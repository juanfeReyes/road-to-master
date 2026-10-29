
import pytest
from starter.agent_skeleton import DATA_DIR, Context, build_graph, ReconState, clear_escalation_file, setup
from langgraph.store.memory import InMemoryStore

from langgraph.checkpoint.memory import MemorySaver
from deepdiff import DeepDiff
from pprint import pprint
from langgraph.runtime import Runtime

config = {"configurable": {"thread_id": "1"}}

def test_reconcile_record():
  setup()
  payment = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                      amount=249.99, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=2,
                      status="reconcile",
                      attempt_history=[],
                      ledger_entry={'amount': 249.99,
                        'currency': 'GBP',
                        'entry_id': 'LED-4001',
                        'entry_type': 'sale',
                        'order_ref': 'ORD-70001',
                        'posted_date': '2026-03-02'})
  expected_state = {'amount': 249.99,
 'attempt_history': [],
 'attempts': 2,
 'currency': 'GBP',
 'evidence': 'Matched on order_ref ORD-70001. Payment amount 249.99 equals '
             'ledger amount 249.99, which is within the 0.02 tolerance. '
             'Currency matches as GBP. Ledger posted on 2026-03-02, the same '
             'calendar day as settlement on 2026-03-02, so timing is '
             'acceptable.',
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
 'status': 'reconciled'}

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  
  result = compiled_graph.nodes["reconcile_node"].invoke(
        payment,
        config=config,
        runtime=Runtime(context=Context(user_id="test_user"), store=store)
    )
  pprint(result)
  assert isinstance(result["evidence"], str) and len(result["evidence"]) > 0
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

def test_amount_tolerance_failed_record():
  clear_escalation_file()
  setup()
  payment = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                      amount=249.80, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=2,
                      status="reconcile",
                      attempt_history=[],
                      ledger_entry={'amount': 249.99,
                        'currency': 'GBP',
                        'entry_id': 'LED-4001',
                        'entry_type': 'sale',
                        'order_ref': 'ORD-70001',
                        'posted_date': '2026-03-02'})
  expected_state = {'amount': 249.8,
 'attempt_history': [],
 'attempts': 2,
 'currency': 'GBP',
 'evidence': 'Payment matches ledger on order_ref ORD-70001 and currency GBP. '
             'However, payment amount 249.80 vs ledger amount 249.99 differs '
             'by 0.19, which exceeds the allowed tolerance of 0.02. Posted '
             'date 2026-03-02 is the same as settled date 2026-03-02, so '
             'timing is not an issue. No duplicate settlement indicated in '
             'settled payment history.',
 'exception_class': 'Amount mismatch',
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
 'status': 'exception'}

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  result = compiled_graph.nodes["reconcile_node"].invoke(
        payment,
        config=config,
        runtime=Runtime(context=Context(user_id="test_user"), store=store)
    )
  pprint(result)
  assert isinstance(result["evidence"], str) and len(result["evidence"]) > 0
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']", "root['attempt_history']"]) == {}

def test_currency_match_failed_record():
  clear_escalation_file()
  setup()
  payment = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                      amount=249.99, currency="USD", 
                      settled_date="2026-03-02", method="card",
                      attempts=2,
                      status="reconcile",
                      attempt_history=[],
                      ledger_entry={'amount': 249.99,
                        'currency': 'GBP',
                        'entry_id': 'LED-4001',
                        'entry_type': 'sale',
                        'order_ref': 'ORD-70001',
                        'posted_date': '2026-03-02'})
  expected_state = {'amount': 249.99,
 'attempt_history': [],
 'attempts': 2,
 'currency': 'USD',
 'evidence': 'Payment PAY-4001 matched ledger entry LED-4001 on order_ref '
             'ORD-70001. Amounts agree exactly at 249.99, which is within the '
             '0.02 tolerance. Posting date 2026-03-02 matches settled date '
             '2026-03-02, so there is no timing issue. However, currencies '
             'differ: payment is USD while ledger entry is GBP. Under the '
             'matching policy, currencies must be identical for '
             'reconciliation, so this is an exception.',
 'exception_class': 'Currency mismatch',
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
 'status': 'exception'}

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  result = compiled_graph.nodes["reconcile_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}},
        runtime=Runtime(context=Context(user_id="test_user"), store=store)
    )
  pprint(result)
  assert isinstance(result["evidence"], str) and len(result["evidence"]) > 0
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

def test_ledger_not_found_record():
  clear_escalation_file()
  setup()
  payment = ReconState(payment_id="PAY-4025", order_ref='ORD-70025', 
                      amount=249.99, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=2,
                      status="reconcile",
                      attempt_history=[{'attempt': 2, 'error': 'Validation Error'}],
                      ledger_entry={'error': 'not_found', 'order_ref': 'ORD-70025'})
  expected_state = {'amount': 249.99,
 'attempts': 2,
 'currency': 'GBP',
 'evidence': 'No ledger entry was found for order_ref ORD-70025, so the '
             'payment cannot be matched under the reconciliation rule '
             'requiring a processor payment to match a ledger entry on '
             'order_ref. Settled payments history is empty, so there is no '
             'evidence of a duplicate settlement. Amount and currency cannot '
             'be validated because the ledger entry is missing.',
 'exception_class': 'Unmatched payment',
 'ledger_entry': {'error': 'not_found', 'order_ref': 'ORD-70025'},
 'method': 'card',
 'order_ref': 'ORD-70025',
 'payment_id': 'PAY-4025',
 'settled_date': '2026-03-02',
 'status': 'exception'}

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  result = compiled_graph.nodes["reconcile_node"].invoke(
        payment,
        config={"configurable": {"thread_id": "1"}},
        runtime=Runtime(context=Context(user_id="test_user"), store=store)
    )
  pprint(result)
  assert isinstance(result["evidence"], str) and len(result["evidence"]) > 0
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']", "root['attempt_history']"]) == {}

def test_duplicated_reconcile_record():
  setup()
  payment_1 = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                      amount=249.99, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=2,
                      status="reconcile",
                      attempt_history=[],
                      ledger_entry={'amount': 249.99,
                        'currency': 'GBP',
                        'entry_id': 'LED-4001',
                        'entry_type': 'sale',
                        'order_ref': 'ORD-70001',
                        'posted_date': '2026-03-02'})
  payment_2 = ReconState(payment_id="PAY-4002", order_ref='ORD-70001', 
                        amount=55.21, currency="GBP", 
                        settled_date="2026-03-02", method="card",
                        attempts=2,
                        status="reconcile",
                        attempt_history=[],
                        ledger_entry={'amount': 55.21,
                          'currency': 'GBP',
                          'entry_id': 'LED-4001',
                          'entry_type': 'sale',
                          'order_ref': 'ORD-70001',
                          'posted_date': '2026-03-02'})
  expected_state = {'amount': 55.21,
 'attempt_history': [],
 'attempts': 2,
 'currency': 'GBP',
 'evidence': 'Ledger entry matches on order_ref ORD-70001, amount 55.21 equals '
             'ledger amount 55.21 within 0.02 tolerance, currency GBP matches, '
             'and posted_date 2026-03-02 is the same as settled_date '
             '2026-03-02 so timing is acceptable. However, settled payment '
             'history already contains another payment_id PAY-4001 for the '
             'same order_ref ORD-70001. More than one payment exists against '
             'the same order where only one is expected, so this is a '
             'Duplicate settlement exception.',
 'exception_class': 'Duplicate settlement',
 'ledger_entry': {'amount': 55.21,
                  'currency': 'GBP',
                  'entry_id': 'LED-4001',
                  'entry_type': 'sale',
                  'order_ref': 'ORD-70001',
                  'posted_date': '2026-03-02'},
 'method': 'card',
 'order_ref': 'ORD-70001',
 'payment_id': 'PAY-4002',
 'settled_date': '2026-03-02',
 'status': 'exception'}

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  config = {"configurable": {"thread_id": "1"}}
  compiled_graph.nodes["reconcile_node"].invoke(
        payment_1,
        config=config,
        runtime=Runtime(context=Context(user_id="test_user"), store=store)
    )
  result = compiled_graph.nodes["reconcile_node"].invoke(
          payment_2,
          config=config,
          runtime=Runtime(context=Context(user_id="test_user"), store=store)
      )
  pprint(result)
  assert isinstance(result["evidence"], str) and len(result["evidence"]) > 0
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']", "root['attempt_history']"]) == {}

def test_negative_amount_should_():
  setup()
  payment = ReconState(payment_id="PAY-4017", order_ref='ORD-70017', 
                      amount=-120.0, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=2,
                      status="reconcile",
                      attempt_history=[],
                      ledger_entry={'amount': -120.0,
                        'currency': 'GBP',
                        'entry_id': 'LED-4017',
                        'entry_type': 'refund',
                        'order_ref': 'ORD-70017',
                        'posted_date': '2026-03-02'})
  expected_state = {'amount': -120.0,
 'attempt_history': [],
 'attempts': 2,
 'currency': 'GBP',
 'evidence': 'Matched payment to ledger on order_ref ORD-70017. Payment amount '
             '-120.0 matches ledger amount -120.0 within the 0.02 tolerance. '
             'Currency matches exactly: GBP. Ledger posted on 2026-03-02, '
             'which is the same day as settlement date 2026-03-02 and '
             'therefore within the allowed 3 calendar day timing window. No '
             'duplicate settlement found in settled payment history.',
 'ledger_entry': {'amount': -120.0,
                  'currency': 'GBP',
                  'entry_id': 'LED-4017',
                  'entry_type': 'refund',
                  'order_ref': 'ORD-70017',
                  'posted_date': '2026-03-02'},
 'method': 'card',
 'order_ref': 'ORD-70017',
 'payment_id': 'PAY-4017',
 'settled_date': '2026-03-02',
 'status': 'reconciled'}

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  
  result = compiled_graph.nodes["reconcile_node"].invoke(
        payment,
        config=config,
        runtime=Runtime(context=Context(user_id="test_user"), store=store)
    )
  pprint(result)
  assert isinstance(result["evidence"], str) and len(result["evidence"]) > 0
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']", "root['attempt_history']"]) == {}

def test_negative_amount_only_in_payment_should_exception():
  setup()
  payment = ReconState(payment_id="PAY-4017", order_ref='ORD-70017', 
                      amount=120.0, currency="GBP", 
                      settled_date="2026-03-02", method="card",
                      attempts=2,
                      status="reconcile",
                      attempt_history=[],
                      ledger_entry={'amount': -120.0,
                        'currency': 'GBP',
                        'entry_id': 'LED-4017',
                        'entry_type': 'refund',
                        'order_ref': 'ORD-70017',
                        'posted_date': '2026-03-02'})
  expected_state = {'amount': 120.0,
 'attempt_history': [],
 'attempts': 2,
 'currency': 'GBP',
 'evidence': 'Matched on order_ref ORD-70017 and currency GBP matches. '
             'However, payment amount is 120.0 while ledger amount is -120.0, '
             'an absolute difference of 240.0, which exceeds the 0.02 '
             'tolerance. Although the ledger entry_type is refund, the payment '
             'is not negative; refunds reconcile normally only when '
             'represented as negative payments with a matching refund ledger '
             'entry. Posted date 2026-03-02 is the same as settled date '
             '2026-03-02, so timing is not an issue.',
 'exception_class': 'Amount mismatch',
 'ledger_entry': {'amount': -120.0,
                  'currency': 'GBP',
                  'entry_id': 'LED-4017',
                  'entry_type': 'refund',
                  'order_ref': 'ORD-70017',
                  'posted_date': '2026-03-02'},
 'method': 'card',
 'order_ref': 'ORD-70017',
 'payment_id': 'PAY-4017',
 'settled_date': '2026-03-02',
 'status': 'exception'}

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  
  result = compiled_graph.nodes["reconcile_node"].invoke(
        payment,
        config=config,
        runtime=Runtime(context=Context(user_id="test_user"), store=store)
    )
  pprint(result)
  assert isinstance(result["evidence"], str) and len(result["evidence"]) > 0
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']", "root['attempt_history']"]) == {}

