"""Starter skeleton: a payment reconciliation agent.

Recommended stack: LangGraph for the loop, pydantic for response validation,
DeepDiff for comparing what you got against what you expected, pytest-asyncio for
the harness. Fill in the TODOs.
"""
from pathlib import Path
from typing import Optional, TypedDict, Literal
from langchain.tools import tool
from langchain.chat_models import init_chat_model
from pydantic import BaseModel, ValidationError
import markdown_to_json
import json
from datetime import date
import csv

DATA_DIR = Path(__file__).resolve().parent.parent / "data"
PAYMENTS = DATA_DIR / "processor_payments.csv"
POLICY = DATA_DIR / "reconciliation_policy.md"

model = init_chat_model(model="", temperature=0.0)
policy_dict = None

def load_policy():
  global policy_dict
  with open(POLICY, 'r') as file:
    policy = file.read()
    policy_dict = json.loads(markdown_to_json.jsonify(policy))
    policy_dict = next(iter(policy_dict.values()))
    

def get_policy_sections(filter_strings: list):
    filtered_dict = {k: v for k, v in policy_dict.iteritems() if any(section in k for section in filter_strings)}
    return "\n".join([f"{k}\n{v}\n" for k, v in filtered_dict.iteritems()])

def load_payments():
    with open(PAYMENTS) as file:
      reader = csv.DictReader(file)
      return [ ReconState(**row) for row in reader ]

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

class ReconState(TypedDict, total=False):
    payment_id: str
    order_ref: str
    amount: float
    currency: str
    settled_date: str
    ledger_entry: Optional[dict]
    attempts: int
    status: str              # reconciled | exception | awaiting_approval | escalated
    exception_class: Optional[str]
    evidence: str

def choose_after_reconcile(state: ReconState) -> Literal["approval", "escalate"]:
    if state.get("status") is "reconciled":
        return "approval"
    return "escalate"

def validate_response(raw: dict) -> dict:
    """TODO: validate a ledger response against the documented contract.

    Return a normalised entry, or raise so the caller can retry with the reason.
    Read the contract in ledger_api.fetch_ledger_entry, then decide how much you
    are willing to trust it. Consider what your agent should do with a response
    that is well-formed JSON but not the shape you asked for.
    """
    try:
      return LedgerApiResponse(**raw)
    except ValidationError as e:
      print(e)
      return {"errors": "\n".join([ err.get("msg") for err in e.errors() ])}
        
    
def fetch_node(state: ReconState) -> ReconState:
    """TODO: call the ledger service and validate the response.

    On failure, record what went wrong and increment `attempts`. The policy sets
    the retry and escalation behaviour - encode it here rather than in a loop
    somewhere else, so it is testable.
    """
    from ledger_api import fetch_ledger_entry
    from asyncio import run

    MAX_RETRIES = 15
    structured_model = model.with_structured_output(LedgerValidationResponse)
    ledger_rules = get_policy_sections(["When the ledger service misbehaves"])

    retry_count = 0
    should_retry = True
    while retry_count < MAX_RETRIES and should_retry:
        ledger_response = run(fetch_ledger_entry(state.get("order_ref")))
        validated_leger = validate_response(ledger_response)
        query = f"""
              Your are grader assistant which follows the ledger service misbehaves policy
              to analyse the validated ledger response.

              Policy: {ledger_rules}
              Validated Ledger response: {validated_leger}

              When validated ledger response fails return the reason of faillure and what should we do next based on Policy.
              When validated ledger response follows policy please return the ledger response.
            """
        result = structured_model.invoke(query)
        state.update(evidence=state.get("evidence")+f"\n\n{result.get("next_step")}")
        state.update(attempts=state.get("attempts"))
        should_retry = result.get("should_retry")

    return state


def reconcile_node(state: ReconState) -> ReconState:
    """TODO: apply the matching rules and set `status` plus `exception_class`.

    Tolerance, timing, currency, duplicates and refunds are all in the policy.
    Attach the evidence Rina will need in order to accept or reject.
    """
    raise NotImplementedError


def approval_node(state: ReconState) -> ReconState:
    """TODO: the human validation gate. Must genuinely halt, not log and continue."""
    raise NotImplementedError


def escalate_node(state: ReconState) -> ReconState:
    """TODO: write the record, every attempt and its outcome, and what you would
    need in order to proceed, to ESCALATION.md."""
    raise NotImplementedError


def build_graph():
    """TODO: wire fetch -> (retry | reconcile | escalate) -> approval."""
    from langgraph.graph import StateGraph, START, END
    builder =  StateGraph(ReconState)

    # Nodes
    builder.add_node("fetch_node", fetch_node)
    builder.add_node("reconcile_node", reconcile_node)
    builder.add_node("approval_node", approval_node)
    builder.add_node("escalate_node", escalate_node)

    #Edges
    builder.add_edge(START, "fetch_node")
    builder.add_edge("fetch_node", "reconcile_node")
    builder.add_conditional_edges("reconcile_node", choose_after_reconcile, {"approval": "approval_node", "escalate": "escalate_node"})
    builder.add_edge("approval_node", END)
    builder.add_edge("escalate_node", END)

    return builder.compile()


def run_all():
    """TODO: run every payment through the graph and produce the reconciliation
    report. The counts must add up to the number of input payments."""
    load_policy()
    payments = load_payments()
    graph = build_graph()

    for payment in payments:
        print(payment)


if __name__ == "__main__":
    run_all()
