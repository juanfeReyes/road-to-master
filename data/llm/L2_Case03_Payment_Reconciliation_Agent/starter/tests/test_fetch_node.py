
import pytest
from starter.agent_skeleton import build_graph, ReconState, setup

from langgraph.checkpoint.memory import MemorySaver
from deepdiff import DeepDiff
from pprint import pprint

def test_valid_response():
  setup()
  payment = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                       amount=249.99, currency="GBP", 
                       settled_date="2026-03-02", method="card", attempts=1)
  expected_fetch_node = {'amount': 249.99,
    'attempts': 2,
    'currency': 'GBP',
    'ledger_entry': {'amount': 249.99,
                    'currency': 'GBP',
                    'entry_id': 'LED-4001',
                    'entry_type': 'sale',
                    'order_ref': 'ORD-70001',
                    'posted_date': '2026-03-02'},
    'method': 'card',
    'order_ref': 'ORD-70001',
    'payment_id': 'PAY-4001',
    'settled_date': '2026-03-02',
    'status': 'reconcile'}

  checkpointer = MemorySaver()
  graph = build_graph()
  compiled_graph = graph.compile(checkpointer=checkpointer)
  result = compiled_graph.nodes["fetch_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}}
    )
  assert DeepDiff(result, expected_fetch_node) == {}

def test_invalid_api_response():
  setup()
  payment = ReconState(payment_id="PAY-4023", order_ref='ORD-70023', 
                       amount=33.25, currency="GBP", 
                       settled_date="2026-03-12", method="card", attempts=1)
  expected_fetch_node = {'amount': 33.25,
 'attempt_history': [{'attempt': 2, 'error': 'Validation Error'}],
 'attempts': 2,
 'currency': 'GBP',
 'ledger_entry': {'error': 'ledger service timeout for ORD-70023'},
 'method': 'card',
 'order_ref': 'ORD-70023',
 'payment_id': 'PAY-4023',
 'settled_date': '2026-03-12',
 'status': 'retry'}

  checkpointer = MemorySaver()
  graph = build_graph()
  compiled_graph = graph.compile(checkpointer=checkpointer)
  result = compiled_graph.nodes["fetch_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}}
    )
  pprint(result)
  assert DeepDiff(result, expected_fetch_node) == {}

def test_invalid_validated_response():
  setup()
  payment = ReconState(payment_id="PAY-4009", order_ref='ORD-70009', 
                       amount=63.2, currency="GBP", 
                       settled_date="2026-03-06", method="card", attempts=1)
  expected_fetch_node = {'amount': 63.2,
 'attempt_history': [{'attempt': 2, 'error': 'Validation Error'}],
 'attempts': 2,
 'currency': 'GBP',
 'ledger_entry': {'data': {'entry': {'amount': 28.2,
                                     'currency': 'GBP',
                                     'entry_id': 'LED-4009',
                                     'entry_type': 'sale',
                                     'order_ref': 'ORD-70009',
                                     'posted_date': '2026-03-06'}},
                  'meta': {'schema': 'v2'}},
 'method': 'card',
 'order_ref': 'ORD-70009',
 'payment_id': 'PAY-4009',
 'settled_date': '2026-03-06',
 'status': 'retry'}

  checkpointer = MemorySaver()
  graph = build_graph()
  compiled_graph = graph.compile(checkpointer=checkpointer)
  result = compiled_graph.nodes["fetch_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}}
    )
  pprint(result)
  assert DeepDiff(result, expected_fetch_node) == {}

def test_invalid_response_max_attempts_is_escalated():
  setup()
  payment = ReconState(payment_id="PAY-4009", order_ref='ORD-70009', 
                       amount=63.2, currency="GBP", 
                       settled_date="2026-03-06", method="card", attempts=2)
  expected_fetch_node = {'amount': 63.2,
 'attempt_history': [{'attempt': 3, 'error': 'Validation Error'}],
 'attempts': 3,
 'currency': 'GBP',
 'ledger_entry': {'data': {'entry': {'amount': 28.2,
                                     'currency': 'GBP',
                                     'entry_id': 'LED-4009',
                                     'entry_type': 'sale',
                                     'order_ref': 'ORD-70009',
                                     'posted_date': '2026-03-06'}},
                  'meta': {'schema': 'v2'}},
 'method': 'card',
 'order_ref': 'ORD-70009',
 'payment_id': 'PAY-4009',
 'settled_date': '2026-03-06',
 'status': 'escalate'}

  checkpointer = MemorySaver()
  graph = build_graph()
  compiled_graph = graph.compile(checkpointer=checkpointer)
  result = compiled_graph.nodes["fetch_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}}
    )
  pprint(result)
  assert DeepDiff(result, expected_fetch_node) == {}

def test_ledger_not_found_response():
  setup()
  payment = ReconState(payment_id="PAY-4025", order_ref='ORD-70025', 
                       amount=249.99, currency="GBP", 
                       settled_date="2026-03-02", method="card", attempts=1)
  expected_fetch_node = {'amount': 249.99,
 'attempt_history': [{'attempt': 2, 'error': 'Validation Error'}],
 'attempts': 2,
 'currency': 'GBP',
 'ledger_entry': {'error': 'not_found', 'order_ref': 'ORD-70025'},
 'method': 'card',
 'order_ref': 'ORD-70025',
 'payment_id': 'PAY-4025',
 'settled_date': '2026-03-02',
 'status': 'retry'}

  checkpointer = MemorySaver()
  graph = build_graph()
  compiled_graph = graph.compile(checkpointer=checkpointer)
  result = compiled_graph.nodes["fetch_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}}
    )
  pprint(result)
  assert DeepDiff(result, expected_fetch_node) == {}

