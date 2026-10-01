"""Starter skeleton: a payment reconciliation agent.

Recommended stack: LangGraph for the loop, pydantic for response validation,
DeepDiff for comparing what you got against what you expected, pytest-asyncio for
the harness. Fill in the TODOs.
"""
from pathlib import Path
from typing import Optional, TypedDict, Literal
import uuid
from langchain.tools import tool
from langchain.chat_models import BaseChatModel, init_chat_model
from pydantic import BaseModel, ValidationError
import markdown_to_json
import json
from datetime import date
import csv
from langgraph.graph import StateGraph, START, END
from pprint import pprint
from portkey_ai import createHeaders
from dotenv import load_dotenv
import os
from langchain_core.runnables import RunnableConfig
from ledger_api import fetch_ledger_entry
from langchain.agents import create_agent
from langgraph.store.memory import InMemoryStore
from dataclasses import dataclass
from langgraph.runtime import Runtime

load_dotenv()

DATA_DIR = Path(__file__).resolve().parent.parent / "data"
PAYMENTS = DATA_DIR / "processor_payments.csv"
POLICY = DATA_DIR / "reconciliation_policy.md"

model: BaseChatModel = None
store: InMemoryStore = None
policy_dict = {}
args = {}

def setup_args():
  from argparse import ArgumentParser
  global args
  parser = ArgumentParser()
  parser.add_argument("--env", dest="env", required=False, default="portkey", choices=["local", "portkey"])
  parser.add_argument("--model", dest="model", required=False, default="@azure-openai-eus2/gpt-5.4")
  parser.add_argument("--user", dest="user", required=False, default="Rina")
  arg, unknown = parser.parse_known_args()
  args = arg

def setup_model():
  global model
  global store
  store = InMemoryStore()

  if args.env == "local":
    model = init_chat_model(
      model="llama3.1:8b",
      model_provider="ollama",
      temperature=0.0
    )

  PORTKEY_API_KEY = os.getenv("PORT_KEY_KEY")
  portkey_headers = createHeaders(api_key=PORTKEY_API_KEY, provider="azure-openai-eus2")
  model = init_chat_model(
    model=args.model, 
    model_provider="openai",
    base_url=os.getenv("PORT_KEY_URL"),
    api_key=PORTKEY_API_KEY,
    default_headers=portkey_headers,
    temperature=0.0
  )

def load_policy():
  global policy_dict
  with open(POLICY, 'r') as file:
    policy = file.read()
    policy_dict = json.loads(markdown_to_json.jsonify(policy))
    policy_dict = next(iter(policy_dict.values()))
    
def get_policy_sections(filter_strings: list):
    filtered_dict = {k: v for k, v in policy_dict.items() if any(section in k for section in filter_strings)}
    return "\n".join([f"{k}\n{v}\n" for k, v in filtered_dict.items()])

def load_payments():
    with open(PAYMENTS) as file:
      reader = csv.DictReader(file)
      return [ ReconState(**row) for row in reader ]

def clear_escalation_file():
    """Clear the contents of ESCALATION.md file."""
    with open(DATA_DIR / "ESCALATION.md", "w") as f:
        f.write("")

@dataclass
class Context:
    user_id: str

class LedgerValidationResponse(BaseModel):
    '''A ledger response with the attempts executed, the next step based on policy and if we should retry getting another response'''
    ledger_response: str
    should_retry: bool
    attempts: int
    next_step: str

class LedgerApiResponse(BaseModel):
    entry_id: str
    order_ref: str
    amount: float
    currency: str
    posted_date: date
    entry_type: str


def choose_after_reconcile(state: ReconState) -> Literal["reconcile", "escalate", "retry"]:
    if state.get("status") == "escalate":
            return "escalate"
    if state.get("status") == "reconcile":
        return "reconcile"
    if state.get("status") == "retry":
            return "retry"
    return END

def call_ledger_api(order_ref: str):
    """Calls ledger API using order_ref to get json"""
    from asyncio import run
    try:
      api_response = run(fetch_ledger_entry(order_ref))
      return api_response
    except Exception as e:
        return {"error": e}

class ReconState(TypedDict, total=False):
    payment_id: str
    order_ref: str
    amount: float
    currency: str
    settled_date: str
    method: str
    ledger_entry: Optional[dict]
    attempts: int
    status: str              # reconciled | exception | awaiting_approval | escalated
    exception_class: Optional[str]
    evidence: str
    attempt_history: Optional[list[dict]]


@tool
def validate_response(raw: dict) -> dict:
    """validate a ledger response against the documented contract"""
    try:
      return LedgerApiResponse.model_validate(raw)
    except ValidationError as e:
      return {"error": "Validation Error"}
    except TypeError as e:
      return {"error": "Type error"}

def fetch_node_agent(state: ReconState) -> ReconState:
    """TODO: call the ledger service and validate the response.

    On failure, record what went wrong and increment `attempts`. The policy sets
    the retry and escalation behaviour - encode it here rather than in a loop
    somewhere else, so it is testable.
    """
    from langchain.agents import create_agent
    
    # structured_model = model.with_structured_output(schema=LedgerValidationResponse)
    ledger_policy = get_policy_sections(["When the ledger service misbehaves"])

    from collections.abc import Callable
    from langchain.agents import create_agent
    from langchain.agents.middleware import wrap_tool_call
    from langchain.messages import ToolMessage
    from langchain.tools.tool_node import ToolCallRequest
    @wrap_tool_call
    def handle_tool_errors(
        request: ToolCallRequest,
        handler: Callable[[ToolCallRequest], ToolMessage],
    ) -> ToolMessage:
        """Convert tool exceptions into ToolMessages the model can handle."""
        try:
            return handler(request)
        except Exception as e:
            return ToolMessage(
                content=f"Tool error: Follow {ledger_policy} to determiner retry",
                tool_call_id=request.tool_call["id"],
            )
    SYSTEM_PROMPT =  """
      You are a helpfull agent to fetch and validate ledger response using payment process.
      You follow the policy misbehave ledger api to handle errors.

      Policy:
        In case of error:
        - Retry up to 3 times
        - After more than 3 failures then reply reason of failure and total number of attempts

      Get ledger data by calling call_ledger_api
      Then validate the data by passing the response to validate_response 
    """
    tools = [call_ledger_api, validate_response]
    agent = create_agent(model=model,
                        tools=tools,
                        middleware=[handle_tool_errors],
                        system_prompt=SYSTEM_PROMPT)

    result = agent.invoke({"messages":
                           [{"role": "user", "content": f"payment to process: {state}"}]
                          })
    print(f"Fetch node agendt result: {result}\n")
 
    return state

def fetch_node(state: ReconState) -> ReconState:
    """TODO: call the ledger service and validate the response.

    On failure, record what went wrong and increment `attempts`. The policy sets
    the retry and escalation behaviour - encode it here rather than in a loop
    somewhere else, so it is testable.
    """

    ledger_policy = get_policy_sections(["When the ledger service misbehaves"])
    ledger_response = call_ledger_api(state.get("order_ref"))   
    SYSTEM_PROMPT =  f"""
          You are a helpfull agent to validate ledger response using payment data.
          You follow the policy misbehave ledger api to handle errors.
    
          Policy: {ledger_policy}

          Follow instructions:
          1. Increase attempts by 1
          2. Give the ledger_response to validate_response() tool. If validate_response tool return contains error field then ledger_response is not valid
          3. Evaluate validate_response result scenarios to provide answer:
            * When validate_response is valid THEN respond a json with ALL payment_data, rename ledger_response as ledger_entry and status equals 'reconcile'
            * When validate_response is invalid and Policy allows more attempts THEN respond a json with ALL payment_data, rename ledger_response as ledger_entry, add error in the attempt_history and status equals 'retry'
            * When validate_response is invalid and Policy NOT allows more attempts THEN respond a json with ALL payment_data, rename ledger_response as ledger_entry, add error in the attempt_history and status equals 'escalate'
        """
    tools = [validate_response]
    agent = create_agent(model=model,
                        tools=tools,
                        system_prompt=SYSTEM_PROMPT)
    result = agent.invoke({"messages":
                               [{"role": "user", "content": f"Validate ledger_response: {ledger_response} and payment data: {state}"}]
                              })
    structured_model = model.with_structured_output(ReconState)
    structured_result = structured_model.invoke(f"Extract response from message {result["messages"][-1].content}")
    return structured_result

def update_store(result: ReconState, runtime: Runtime[Context]):
  user_id = runtime.context.user_id

  namespace = (user_id, "settled_payments")

  settled_payment_id = str(uuid.uuid4())
  settled_payment = {"payment_id": result["payment_id"],
                    "order_ref": result["order_ref"]}
  runtime.store.put(namespace, settled_payment_id, settled_payment)

def get_settled_payments(runtime: Runtime[Context]):
  user_id = runtime.context.user_id
  namespace = (user_id, "settled_payments")
  return [pay.value for pay in runtime.store.search(namespace)]

def reconcile_node(state: ReconState, runtime: Runtime[Context]) -> ReconState:
    """apply the matching rules and set `status` plus `exception_class`.

    Tolerance, timing, currency, duplicates and refunds are all in the policy.
    Attach the evidence Rina will need in order to accept or reject.
    """
    # TODO: Improve formating for policy to make them more readable
    settled_payments = get_settled_payments(runtime)
    matching_rules_policy = get_policy_sections(["Matching", "Amount tolerance", "Timing"] )
    exception_policy = get_policy_sections(["Exception classes"])

    SYSTEM_PROMPT =  f"""
    You are a helpfull agent to analyse payment data and ledger data to determine if they reconcile or not.
    
    Follow instructions:
    1. Use matching rules policy: {matching_rules_policy} and history of settled payments to determine if payment data and ledger data reconcile or not.
    2. Set exception_class using exception policy: {exception_policy} if payment data and ledger data do not reconcile.
    3. If payment data and ledger data reconcile THEN respond a json with ALL payment_data, evidence why they reconcile, no exception_class and status equals 'reconciled'
    4. If payment data and ledger data do not reconcile THEN respond a json with ALL payment_data, evidence why they do not reconcile, set exception_class and status equals 'exception'
    """
    structured_model = model.with_structured_output(ReconState)
    result = structured_model.invoke([{"role": "system", "content": SYSTEM_PROMPT},
                          {"role": "user", "content": f"Payment data: {state} and history of settled payments: {settled_payments}"}
                          ])
    if result.get("status") == "reconciled":
        update_store(result, runtime)
        settled_payments = get_settled_payments(runtime)
    return result
    


def approval_node(state: ReconState) -> ReconState:
    """TODO: the human validation gate. Must genuinely halt, not log and continue."""
    return state


def escalate_node(state: ReconState) -> ReconState:
    """write the record, every attempt and its outcome, and what you would
    need in order to proceed, to ESCALATION.md."""

    SYSTEM_PROMPT =  """
    You are a helpfull agent to write a summary report as markdown following template:
     
    Title: <Payment ID> - Escalation report
    Error analysis: Analyze the attempt_history and its outcome
    How to proceed next: Describe what you would need in order to proceed.

    Write a short summary between 100 and 200 words.
    """
    result = model.invoke([{"role": "system", "content": SYSTEM_PROMPT},
                          {"role": "user", "content": f"Payment data: {state}"}
                          ])
    
    escalation_message = result.content
    ESCALARTION_FILE = DATA_DIR / "ESCALATION.md"

    with open(ESCALARTION_FILE, "a+") as f:
        f.write(f"\n{escalation_message}\n")
    state.update(evidence=f"Please check ESCALATION.md for details.",status="escalated")
    return state


def build_graph():
    """TODO: wire fetch -> (retry | reconcile | escalate) -> approval."""
    builder =  StateGraph(ReconState)

    # Nodes
    builder.add_node("fetch_node", fetch_node)
    builder.add_node("reconcile_node", reconcile_node)
    builder.add_node("approval_node", approval_node)
    builder.add_node("escalate_node", escalate_node)

    #Edges
    builder.add_edge(START, "fetch_node")
    builder.add_conditional_edges("fetch_node", 
                                  choose_after_reconcile, 
                                  {"reconcile": "reconcile_node",
                                    "escalate": "escalate_node",
                                    "retry": "fetch_node"})
    builder.add_edge("reconcile_node", "approval_node")
    builder.add_edge("escalate_node", "approval_node")
    builder.add_edge("approval_node", END)

    return builder

def setup():
    setup_args()
    setup_model()
    load_policy()


def run_all():
    """run every payment through the graph and produce the reconciliation
    report. The counts must add up to the number of input payments."""
    from langgraph.checkpoint.memory import InMemorySaver
    setup()
    payments = load_payments()
    checkpointer = InMemorySaver()
    graph = build_graph().compile(checkpointer=checkpointer, store=store)
    clear_escalation_file()

    for payment in payments:
        config = {"configurable": {"thread_id": "1"}}
        payment.update(attempts=0)
        graph.invoke(payment, config=config, context=Context(user_id=args.user))


if __name__ == "__main__":
    run_all()
