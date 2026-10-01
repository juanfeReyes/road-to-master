
import pytest
from starter.agent_skeleton import DATA_DIR, build_graph, ReconState, clear_escalation_file, setup

from langgraph.checkpoint.memory import MemorySaver
from deepdiff import DeepDiff
from pprint import pprint

def test_add_escalation_report():
  clear_escalation_file()
  setup()
  payment = ReconState(payment_id="PAY-4023", order_ref='ORD-70023', 
                      amount=249.99, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=3,
                      status="escalate",
                      attempt_history=[
                        {'attempt': 1, 'error': 'Validation Error'},
                        {'attempt': 2, 'error': 'Validation Error'},
                        {'attempt': 3, 'error': 'Validation Error'}],
                      ledger_entry={'error': 'ledger service timeout for ORD-70023'})
  expected_state = {'amount': 249.99,
 'attempt_history': [{'attempt': 1, 'error': 'Validation Error'},
                     {'attempt': 2, 'error': 'Validation Error'},
                     {'attempt': 3, 'error': 'Validation Error'}],
 'attempts': 3,
 'currency': 'GBP',
 'evidence': 'Please check ESCALATION.md for details.',
 'ledger_entry': {'error': 'ledger service timeout for ORD-70023'},
 'method': 'card',
 'order_ref': 'ORD-70023',
 'payment_id': 'PAY-4023',
 'settled_date': '2026-03-02',
 'status': 'escalated'}

  checkpointer = MemorySaver()
  graph = build_graph()
  compiled_graph = graph.compile(checkpointer=checkpointer)
  result = compiled_graph.nodes["escalate_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}}
    )
  assert DeepDiff(result, expected_state) == {}

def test_append_escalation_report():
  clear_escalation_file()
  setup()
  payment_1 = ReconState(payment_id="PAY-4023", order_ref='ORD-70023', 
                      amount=249.99, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=3,
                      status="escalate",
                      attempt_history=[
                        {'attempt': 1, 'error': 'Validation Error'},
                        {'attempt': 2, 'error': 'Validation Error'},
                        {'attempt': 3, 'error': 'Validation Error'}],
                      ledger_entry={'error': 'ledger service timeout for ORD-70023'})
  payment_2 = ReconState(payment_id="PAY-4024", order_ref='ORD-70024', 
                      amount=149.99, currency="USD", 
                      settled_date="2026-03-03", method="check",
                      attempts=2,
                      status="escalate",
                      attempt_history=[
                        {'attempt': 1, 'error': 'Timeout error for ledger service'},
                        {'attempt': 2, 'error': 'Validation Error: invalid method check'}],
                      ledger_entry={'error': 'ledger service timeout for ORD-70024'})
  expected_state = {'amount': 149.99,
  'attempt_history': [{'attempt': 1,
                        'error': 'Timeout error for ledger service'},
                      {'attempt': 2,
                        'error': 'Validation Error: invalid method check'}],
  'attempts': 2,
  'currency': 'USD',
  'evidence': 'Please check ESCALATION.md for details.',
  'ledger_entry': {'error': 'ledger service timeout for ORD-70024'},
  'method': 'check',
  'order_ref': 'ORD-70024',
  'payment_id': 'PAY-4024',
  'settled_date': '2026-03-03',
  'status': 'escalated'}

  checkpointer = MemorySaver()
  graph = build_graph()
  compiled_graph = graph.compile(checkpointer=checkpointer)
  compiled_graph.nodes["escalate_node"].invoke(
        payment_1,
        config={"configurable": {"thread_id": "1"}}
    )
  result = compiled_graph.nodes["escalate_node"].invoke(
          payment_2,
          config={"configurable": {"thread_id": "1"}}
      )
  assert DeepDiff(result, expected_state) == {}
