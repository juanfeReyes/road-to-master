import time
import re

from .models import EvaluationRuntimeModelConfig, GroundedResponse
from langchain.messages import HumanMessage
from .retrieval import LocalRetriever
from .scoring import score_response
from deepagents import create_deep_agent
from langchain.agents.middleware import PIIMiddleware

def answer_question(question: str, retriever: LocalRetriever, chat_model: str, runtime_config: EvaluationRuntimeModelConfig | None = None, portkey_api_key: str | None = None) -> GroundedResponse:
    if not question.strip():
        raise ValueError("Question cannot be blank.")
    start_time = time.perf_counter()
    docs = retriever.search(question)
    docs_string = "\n\n".join(f"[{doc.metadata.get("source")}]\n{doc.page_content}" for doc in docs)
    instructions = f"""You are a helpful assistant who is good at analyzing source information and answering questions.
       Use the following source documents to answer the user's questions.
       If you don't know the answer, just say that you don't know.
       Use three sentences maximum and keep the answer concise.
       For every procedure retrieved provide safety guidelines.

    <context>
    {docs_string}
    </context>"""
    model = build_model(chat_model, runtime_config, portkey_api_key)
    agent = create_deep_agent(
      model=model,
      system_prompt=instructions,
      middleware=[PIIMiddleware(
          "email",
          strategy="redact",
          apply_to_input=True,
      )]
    )
    sanitized_question = sanitize_question(question)
    print(f"Evaluate question: {sanitized_question}")
    result = agent.invoke({
        "messages": [HumanMessage(content=sanitized_question)],
    })

    end_time = time.perf_counter()
    execution_time = end_time - start_time
    answer = result["messages"][-1].content
    input_tokens = result["messages"][-1].usage_metadata.get("input_tokens", 0)
    output_tokens = result["messages"][-1].usage_metadata.get("output_tokens", 0)
    sources = list(dict.fromkeys(doc.metadata.get("source") for doc in docs))
    return GroundedResponse(answer.strip(), 
                            sources, docs, 0,
                            "", execution_time,
                            input_tokens, output_tokens)

def build_model(chat_model, runtime_config, portkey_api_key):
    if runtime_config != None and runtime_config.model_source.source == "portkey":
        from langchain_openai import ChatOpenAI
        from portkey_ai import createHeaders
        portkey_headers = createHeaders(api_key=portkey_api_key,provider="azure-openai-eus2")
        return ChatOpenAI(model=chat_model, api_key=portkey_api_key, base_url=runtime_config.portkey.base_url, default_headers=portkey_headers)
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

