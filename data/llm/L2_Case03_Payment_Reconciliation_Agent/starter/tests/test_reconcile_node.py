
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
 'evidence': 'Matched payment to ledger on order_ref ORD-70001. Currency '
             'matches: GBP = GBP. Settlement date 2026-03-02 and posted date '
             '2026-03-02 are aligned, so no timing exception. Amount '
             'comparison: payment 249.80 vs ledger 249.99, absolute difference '
             '= 0.19, which exceeds the allowed tolerance of 0.02. Therefore '
             'this does not reconcile.',
 'exception_class': 'Amount mismatch beyond tolerance.',
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
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

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
 'evidence': 'Matched on order_ref ORD-70001. Amounts agree within tolerance: '
             'payment 249.99 vs ledger 249.99, difference 0.00. However, '
             'currencies are not identical: payment USD vs ledger GBP. Posting '
             'date 2026-03-02 matches settlement date 2026-03-02, so timing is '
             'not an issue. Therefore this does not reconcile due to currency '
             'mismatch.',
 'exception_class': '**Currency mismatch** between payment and ledger entry.',
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
 'attempt_history': [{'attempt': 2, 'error': 'Validation Error'}],
 'attempts': 2,
 'currency': 'GBP',
 'evidence': 'No ledger entry was found for order_ref ORD-70025, so the '
             'payment cannot be matched under the policy. History of settled '
             'payments is empty, so there is no evidence of a duplicate '
             'settlement. Amount, currency, and timing checks cannot be '
             'satisfied because there is no matching ledger entry.',
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
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

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
 'evidence': 'Payment matches ledger on order_ref ORD-70001. Amount matches '
             'exactly at 55.21, currency matches GBP, and ledger posted on '
             '2026-03-02 which is within 3 calendar days of settlement date '
             '2026-03-02, so the payment reconciles to the ledger entry. '
             'However, settled payment history already contains payment_id '
             'PAY-4001 for the same order_ref ORD-70001, indicating more than '
             'one settled payment against the same order where only one is '
             'expected.',
 'exception_class': '**Duplicate settlement** - more than one payment against '
                    'the same order where\n'
                    'only one is expected.',
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
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

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
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

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
 'evidence': 'Matched payment to ledger on order_ref ORD-70017. Currency '
             'matches: GBP = GBP. Settlement date 2026-03-02 and posted date '
             '2026-03-02 are within timing rules. However, payment amount '
             '120.00 and ledger amount -120.00 differ by 240.00, which exceeds '
             'the allowed tolerance of 0.02. No duplicate settlement found in '
             'settled payment history.',
 'exception_class': 'Amount mismatch beyond tolerance.',
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
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

