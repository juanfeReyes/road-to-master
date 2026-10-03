import pytest

from starter.agent_skeleton import get_policy_sections, load_policy

def test_get_policy():
  expected_policy = """## 4. Exception classes to report

- **Amount mismatch** beyond tolerance.
- **Currency mismatch** between payment and ledger entry.
- **Unmatched payment** - a payment with no ledger entry.
- **Duplicate settlement** - more than one payment against the same order where
  only one is expected.

Refunds appear as negative payments with a matching `refund` ledger entry. They
are normal and reconcile like any other entry."""
  load_policy()
  policies = get_policy_sections(["Exception classes to report"])
  assert policies.startswith(expected_policy)

