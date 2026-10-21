import time
import re

from .models import EvaluationRecord, EvaluationRuntimeModelConfig, GroundedResponse
from langchain.messages import HumanMessage, ToolMessage
from .retrieval import LocalRetriever
from deepagents import create_deep_agent
from langchain.agents.middleware import PIIMiddleware


def build_model(chat_model, runtime_config, portkey_api_key):
    if runtime_config != None and runtime_config.model_source.source == "portkey":
        from langchain_openai import ChatOpenAI
        from portkey_ai import createHeaders
        portkey_headers = createHeaders(api_key=portkey_api_key,provider="azure-openai-eus2")
        return ChatOpenAI(model=chat_model,
                          api_key=portkey_api_key, 
                          base_url=runtime_config.portkey.base_url,
                          default_headers=portkey_headers,
                          use_responses_api=True,
                          temperature=0.0)
    else:
        from langchain_ollama import ChatOllama
        return ChatOllama(model=chat_model, temperature=0.0)

def sanitize_question(text: str) -> str:
    # Remove excessive whitespace and newlines
    text = re.sub(r'\s+', ' ', text)
    # Remove repetitive punctuation like consecutive dots
    text = re.sub(r'\.{2,}', '.', text)
    # Strip leading/trailing spaces
    return text.strip()

# AGENT Generator
from langgraph.graph import MessagesState
from langchain.chat_models import init_chat_model
from typing import Literal, TypedDict
from pydantic import BaseModel, Field
from langchain.messages import HumanMessage, AIMessage, ToolMessage, SystemMessage
from langgraph.graph import END, START, StateGraph
from langgraph.prebuilt import ToolNode
from langchain_core.documents import Document


response_model = None
retriever_tool = None
grader_model = None
model_source = None

def agent_answer_question(record: EvaluationRecord, 
                          retriever: LocalRetriever, chat_model: str, 
                          runtime_config: EvaluationRuntimeModelConfig | None = None, portkey_api_key: str | None = None):

    question = record.input
    start_time = time.perf_counter()
    model = build_model(chat_model, runtime_config, portkey_api_key)
    global response_model
    global retriever_tool
    global grader_model
    global model_source
    model_source = runtime_config.model_source.source
    response_model=model
    grader_model=model
    retriever_tool=retriever.get_search_tool()

    graph = build_graph()
    input_tokens = 0
    output_tokens = 0
    sanitized_question = sanitize_question(question)
    docs = retriever.get_docs(sanitized_question)
    print(f"Evaluate question: {sanitized_question}")

    from langchain.messages import HumanMessage
    messages = [HumanMessage(content=question)]
    response = None
    try:
      messages = graph.invoke({"messages": messages, "count": 0})
      response = messages["messages"][-1].content
    except:
       response = "It was not possible to process your question. Please try again with another configuration"

    end_time = time.perf_counter()
    execution_time = end_time - start_time
    return GroundedResponse(record, response.strip(), 
                                docs, execution_time,
                                input_tokens, output_tokens)

def get_agent_response(result):
   return result["messages"][-1].content[-1]["text"] if model_source == "portkey" else result["messages"][-1].content

def get_model_response(result):
   return result.content[-1]["text"] if model_source == "portkey" else result.content.replace("**Answer:**","")


def build_graph():
  workflow = StateGraph(AgentState)

  workflow.add_node("query", query)
  workflow.add_node("grade_response", grade_response)
  workflow.add_node("rewrite_question", rewrite_question)
  workflow.add_node("resolve_conflicts", resolve_conflicts)
  workflow.add_node("get_safety_measures", get_safety_measures)
  workflow.add_node("sumarize_response", sumarize_response)

  def route(state: AgentState):
      veredict = state["veredict"]
      count = state["count"]

      if count > 3:
          return END

      return veredict
      

  workflow.add_edge(START, "query")
  workflow.add_edge("query", "grade_response")
  workflow.add_conditional_edges("grade_response", route, {
      "Yes": "get_safety_measures",
      "Contradict": "resolve_conflicts",
      "No": "rewrite_question"
      })
  workflow.add_edge("rewrite_question", "query")
  workflow.add_edge("resolve_conflicts", "get_safety_measures")
  workflow.add_edge("get_safety_measures", "sumarize_response")
  workflow.add_edge("sumarize_response", END)
  
  return workflow.compile()
  
def query(state: AgentState):
    from langchain.agents import create_agent
    SYSTEM_PROMT = SystemMessage(content="""
      You are a helpful agent to query technical data from context.

      Retrieve context from the retriever_tool only once and use only the information retrieved to answer the question
      Reply in text format with max 100 to 150 words.
      
      Use the following template for your response:
      Question: <question>
      Context: <list_of_documents>
      Response: <response>
      Sources: <list_source_documents>     
    """)
    
    agent = create_agent(
      model=response_model,
      tools=[retriever_tool],
      system_prompt=SYSTEM_PROMT
    )
    result = agent.invoke({"messages": [SYSTEM_PROMT] + state["messages"] })
    response = get_agent_response(result)
    return {"messages": [response], "count": state["count"]+1}

def grade_response(state: AgentState):
    SYSTEM_PROMT = SystemMessage(content="""
      You are Grader agent to analyze the Context, Response and Question to review semantic and meaning

      Follow strictly next instructions to grade the Response based on context:
      - Reply strictly 'Yes' if Response is relevant to documents in Context
      - Reply strictly 'Contradict' if the Response has contradictions between different documents in Context 
      - Reply strictly 'No' if cases above do not match
    """)
    result = grader_model.invoke([SYSTEM_PROMT]
        + state["messages"]
    )
    response = get_model_response(result)
    return {"messages": state["messages"], "count": state["count"], "veredict": response}

def rewrite_question(state: AgentState):
  SYSTEM_PROMT = SystemMessage(content="""
    You are a rewriter agent that uses the Question, Context and Response to rewrite a question with more specific details related to the original question.
  """)
  result = grader_model.invoke([SYSTEM_PROMT]
      + state["messages"]
  )
  response = get_model_response(result)
  return {"messages": [response], "count": state["count"], "veredict": state["veredict"]}

def resolve_conflicts(state: AgentState):
  SYSTEM_PROMT = SystemMessage(content="""
  You are conflict resolver agent that reason to resolve the conflicts between the documents in the context.

  Use the following template for your response:
  Question: <question>
  Context: <list_of_documents>
  Response: <response>
  Sources: <list_source_documents>  
  """)
  result = grader_model.invoke([SYSTEM_PROMT]
      + state["messages"]
  )
  response = get_model_response(result)
  return {"messages": [response], "count": state["count"], "veredict": state["veredict"]}

def get_safety_measures(state: AgentState):
    from langchain.agents import create_agent
    SYSTEM_PROMT = SystemMessage(content="""
      You are a helpful agent to get the safety measures from de Question, Context and Response

      Retrieve the safety measures from the retriever_tool and use only the information retrieved to answer the question
      Reply in text format with max 100 to 150 words.
      
      Use the following template for your response:
      Question: <question>
      Context: <list_of_documents>
      Response: <response>
      Sources: <list_source_documents>
      Safety Measures: <safety_measures>   
    """)
    
    agent = create_agent(
      model=response_model,
      tools=[retriever_tool],
      system_prompt=SYSTEM_PROMT
    )
    result = agent.invoke({"messages": [SYSTEM_PROMT] + state["messages"] })
    response = get_agent_response(result)
    # print(f"Safety measures: {response}")
    return {"messages": [response], "count": state["count"]+1, "veredict": state["veredict"]}

def sumarize_response(state: AgentState):
  SYSTEM_PROMT = SystemMessage(content="""
    You are a sumarizer agent that will sumarize the content to 100 to 200 words.
    Important to REMOVE the Context section from the response.

    Follow strictly the text template to reply:

    Response: <response_section_witout_context>
    Safety Measures: <safety_measures>
    Sources: <list_source_documents>
  """)
  result = grader_model.invoke([SYSTEM_PROMT]
      + state["messages"]
  )
  response = get_model_response(result)
  return {"messages": [response], "count": state["count"], "veredict": state["veredict"]}

class AgentState(MessagesState):
    count: int
    veredict: str

