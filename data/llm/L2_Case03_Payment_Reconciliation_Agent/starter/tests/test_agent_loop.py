import pytest
from starter.agent_skeleton import DATA_DIR, Context, build_graph, ReconState, clear_escalation_file, clear_reconciliation_file, clear_reconciliation_file, setup
from langgraph.store.memory import InMemoryStore

from langgraph.checkpoint.memory import MemorySaver
from deepdiff import DeepDiff
from pprint import pprint
from langgraph.runtime import Runtime
from langgraph.types import Command
from unittest.mock import patch

config = {"configurable": {"thread_id": "1"}}

@patch('starter.agent_skeleton.fetch_ledger_entry')
def test_reconciled_payment_approved_then_approved(api_mock):
  clear_escalation_file()
  clear_reconciliation_file()
  setup()
  api_mock.return_value = {"entry_id": "FAKE-4001",
                            "order_ref": "ORD-70001",
                            "amount": 249.99,
                            "currency": "GBP",
                            "posted_date": "2026-03-02",
                            "entry_type": "sale"}
  decision = "Yes"
  expected_state = {'amount': 249.99,
  'attempt_history': [],
  'attempts': 3,
  'currency': 'GBP',
  'evidence': 'Matched on order_ref ORD-70001; identical amount 249.99 GBP; '
              'same-day posting and settlement on 2026-03-02; no duplicate '
              'found in settled payment history.',
  'ledger_entry': {'amount': 249.99,
                    'currency': 'GBP',
                    'entry_id': 'FAKE-4001',
                    'entry_type': 'sale',
                    'order_ref': 'ORD-70001',
                    'posted_date': '2026-03-02'},
  'method': 'card',
  'order_ref': 'ORD-70001',
  'payment_id': 'PAY-4001',
  'settled_date': '2026-03-02',
  'status': 'approved'}

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

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  stream = compiled_graph.stream_events(payment, config=config, context=Context(user_id="test_user"), version="v3")
  stream.output
  resumed = compiled_graph.stream_events(Command(resume=decision), config=config, context=Context(user_id="test_user"), version="v3")
  result = resumed.output
  pprint(result)
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

@patch('starter.agent_skeleton.fetch_ledger_entry')
def test_amount_tolerance_payment_rejected_then_awaiting_approved(api_mock):
  clear_escalation_file()
  clear_reconciliation_file()
  setup()
  api_mock.return_value = {"entry_id": "FAKE-4001",
                            "order_ref": "ORD-70001",
                            "amount": 249.70,
                            "currency": "GBP",
                            "posted_date": "2026-03-02",
                            "entry_type": "sale"}
  decision = "Reject"
  expected_state = {'amount': 249.99,
 'attempt_history': [],
 'attempts': 3,
 'currency': 'GBP',
 'evidence': 'Matched payment to ledger entry on order_ref ORD-70001. Currency '
             'matches: GBP = GBP. Amount comparison failed: payment amount '
             '249.99 vs ledger amount 249.70, absolute difference 0.29, which '
             'exceeds the allowed tolerance of 0.02. Posting date 2026-03-02 '
             'is the same as settled date 2026-03-02, so timing is not an '
             'issue. No duplicate settlement indicated in settled payment '
             'history. Reconciliation report status updated to '
             'awaiting_approval. Recommendation: Reject the payment '
             'settlement. User answer: Reject.',
 'exception_class': 'Amount mismatch beyond tolerance.',
 'ledger_entry': {'amount': 249.7,
                  'currency': 'GBP',
                  'entry_id': 'FAKE-4001',
                  'entry_type': 'sale',
                  'order_ref': 'ORD-70001',
                  'posted_date': '2026-03-02'},
 'method': 'card',
 'order_ref': 'ORD-70001',
 'payment_id': 'PAY-4001',
 'settled_date': '2026-03-02',
 'status': 'awaiting_approval'}

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

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  stream = compiled_graph.stream_events(payment, config=config, context=Context(user_id="test_user"), version="v3")
  stream.output
  resumed = compiled_graph.stream_events(Command(resume=decision), config=config, context=Context(user_id="test_user"), version="v3")
  result = resumed.output
  pprint(result)
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

@patch('starter.agent_skeleton.fetch_ledger_entry')
def test_currency_match_payment_rejected_then_awaiting_approved(api_mock):
  clear_escalation_file()
  clear_reconciliation_file()
  setup()
  api_mock.return_value = {"entry_id": "FAKE-4001",
                            "order_ref": "ORD-70001",
                            "amount": 249.99,
                            "currency": "COL",
                            "posted_date": "2026-03-02",
                            "entry_type": "sale"}
  decision = "Reject"
  expected_state = {'amount': 249.99,
 'attempt_history': [],
 'attempts': 3,
 'currency': 'GBP',
 'evidence': 'Order reference matched, amounts reconcile exactly, and dates '
             'align, but currencies differ: payment GBP versus ledger COL.',
 'exception_class': 'Currency mismatch between payment and ledger entry.',
 'ledger_entry': {'amount': 249.99,
                  'currency': 'COL',
                  'entry_id': 'FAKE-4001',
                  'entry_type': 'sale',
                  'order_ref': 'ORD-70001',
                  'posted_date': '2026-03-02'},
 'method': 'card',
 'order_ref': 'ORD-70001',
 'payment_id': 'PAY-4001',
 'settled_date': '2026-03-02',
 'status': 'awaiting_approval'}

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

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  stream = compiled_graph.stream_events(payment, config=config, context=Context(user_id="test_user"), version="v3")
  stream.output
  resumed = compiled_graph.stream_events(Command(resume=decision), config=config, context=Context(user_id="test_user"), version="v3")
  result = resumed.output
  pprint(result)
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

# TODO: Fix this scenario
@patch('starter.agent_skeleton.fetch_ledger_entry')
def test_ledger_not_found_payment_rejected_then_awaiting_approved(api_mock):
  clear_escalation_file()
  clear_reconciliation_file()
  setup()
  api_mock.return_value = {"error": "not_found", "order_ref": "ORD-70001"}
  decision = "Reject"
  expected_state = {'amount': 249.99,
  'attempt_history': [],
  'attempts': 3,
  'currency': 'GBP',
  'evidence': 'Matched on order_ref ORD-70001; identical amount 249.99 GBP; '
              'same-day posting and settlement on 2026-03-02; no duplicate '
              'found in settled payment history.',
  'ledger_entry': {'amount': 249.99,
                    'currency': 'GBP',
                    'entry_id': 'FAKE-4001',
                    'entry_type': 'sale',
                    'order_ref': 'ORD-70001',
                    'posted_date': '2026-03-02'},
  'method': 'card',
  'order_ref': 'ORD-70001',
  'payment_id': 'PAY-4001',
  'settled_date': '2026-03-02',
  'status': 'approved'}

  payment = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                        amount=249.99, currency="GBP", 
                        settled_date="2026-03-02", method="card",
                        attempts=0,
                        status="reconcile",
                        attempt_history=[])

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  stream = compiled_graph.stream_events(payment, config=config, context=Context(user_id="test_user"), version="v3")
  stream.output
  resumed = compiled_graph.stream_events(Command(resume=decision), config=config, context=Context(user_id="test_user"), version="v3")
  result = resumed.output
  pprint(result)
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

# TODO: fix flaky test, maybe after improving the policy extraction
@patch('starter.agent_skeleton.fetch_ledger_entry')
def test_duplicated_reconcile_payment_approved_then_awaiting_approved(api_mock):
  clear_escalation_file()
  clear_reconciliation_file()
  setup()
  api_mock.return_value = {"entry_id": "FAKE-4001",
                            "order_ref": "ORD-70001",
                            "amount": 249.99,
                            "currency": "GBP",
                            "posted_date": "2026-03-02",
                            "entry_type": "sale"}
  decision = "Approve please"
  expected_state = {'amount': 249.99,
 'attempt_history': [],
 'attempts': 1,
 'currency': 'GBP',
 'evidence': 'Payment matches ledger entry on order_ref ORD-70001 with '
             'identical amount 249.99 and currency GBP, and posted_date '
             '2026-03-02 is within timing rules. However, settled payments '
             'history already contains payment_id PAY-4001 for the same '
             'order_ref ORD-70001, so this is more than one payment against '
             'the same order where only one is expected.',
 'exception_class': 'Duplicate settlement',
 'ledger_entry': {'amount': 249.99,
                  'currency': 'GBP',
                  'entry_id': 'FAKE-4001',
                  'entry_type': 'sale',
                  'order_ref': 'ORD-70001',
                  'posted_date': '2026-03-02'},
 'method': 'card',
 'order_ref': 'ORD-70001',
 'payment_id': 'PAY-4002',
 'settled_date': '2026-03-02',
 'status': 'awaiting_approval'}

  payment_1 = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                        amount=249.99, currency="GBP", 
                        settled_date="2026-03-02", method="card",
                        attempts=0,
                        attempt_history=[])

  payment_2 = ReconState(payment_id="PAY-4002", order_ref='ORD-70001', 
                          amount=249.99, currency="GBP", 
                          settled_date="2026-03-02", method="card",
                          attempts=0,
                          attempt_history=[])

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)

  # Payment 1
  stream = compiled_graph.stream_events(payment_1, config=config, context=Context(user_id="test_user"), version="v3")
  stream.output
  resumed = compiled_graph.stream_events(Command(resume=decision), config=config, context=Context(user_id="test_user"), version="v3")
  result = resumed.output

  # Payment 2
  stream = compiled_graph.stream_events(payment_2, config=config, context=Context(user_id="test_user"), version="v3")
  stream.output
  resumed = compiled_graph.stream_events(Command(resume="Reject"), config=config, context=Context(user_id="test_user"), version="v3")
  result = resumed.output
  pprint(result)
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}

@patch('starter.agent_skeleton.fetch_ledger_entry')
def test_escalated_payment_approved_then_approved(api_mock):
  clear_escalation_file()
  clear_reconciliation_file()
  setup()
  api_mock.return_value = {"entry_id": "FAKE-4001",
                            "order_ref": "ORD-70001",
                            "amount": 249.70,
                            "posted_date": "2026-15-02",
                            "entry_type": "sale"}
  decision = "Reject"
  expected_state = {'amount': 249.99,
 'attempt_history': [{'attempt': 1, 'error': 'Validation Error'},
                     {'attempt': 2, 'error': 'Validation Error'},
                     {'attempt': 3, 'error': 'Validation Error'}],
 'attempts': 3,
 'currency': 'GBP',
 'evidence': 'Order reference matches ORD-70001, but amount mismatch, invalid '
             'ledger date, escalated status, and repeated validation failures '
             'are documented. Evidence note references ESCALATION.md for '
             'further details.',
 'exception_class': 'Validation Error',
 'ledger_entry': {'amount': 249.7,
                  'entry_id': 'FAKE-4001',
                  'entry_type': 'sale',
                  'order_ref': 'ORD-70001',
                  'posted_date': '2026-15-02'},
 'method': 'card',
 'order_ref': 'ORD-70001',
 'payment_id': 'PAY-4001',
 'settled_date': '2026-03-02',
 'status': 'escalated'}

  payment = ReconState(payment_id="PAY-4001", order_ref='ORD-70001', 
                        amount=249.99, currency="GBP", 
                        settled_date="2026-03-02", method="card",
                        attempts=0,
                        status="reconcile",
                        attempt_history=[])

  checkpointer = MemorySaver()
  graph = build_graph()
  store = InMemoryStore()
  compiled_graph = graph.compile(checkpointer=checkpointer, store=store)
  stream = compiled_graph.stream_events(payment, config=config, context=Context(user_id="test_user"), version="v3")
  stream.output
  resumed = compiled_graph.stream_events(Command(resume=decision), config=config, context=Context(user_id="test_user"), version="v3")
  result = resumed.output
  pprint(result)
  assert DeepDiff(result, expected_state, exclude_paths=["root['evidence']"]) == {}
