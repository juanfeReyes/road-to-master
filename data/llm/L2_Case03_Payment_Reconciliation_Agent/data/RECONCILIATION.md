
Title: PAY-4001 - Payment settlement summary

Reasoning: Payment PAY-4001 is not reconciled. While the order reference matches between the payment and ledger entry, there are material discrepancies preventing clean settlement: the ledger amount is 249.70 GBP versus the payment amount of 249.99 GBP, creating a 0.29 GBP mismatch; the ledger entry is missing a currency field; and the posted_date value `2026-15-02` is invalid. The payment has already failed reconciliation validation across 3 attempts and is currently escalated.

Evidence: 
- Payment ID: PAY-4001
- Order reference: ORD-70001
- Payment amount: 249.99 GBP
- Settlement date: 2026-03-02
- Method: card
- Ledger entry ID: FAKE-4001
- Ledger order reference: ORD-70001
- Ledger amount: 249.70
- Ledger entry type: sale
- Ledger posted date: `2026-15-02` (invalid date)
- Ledger currency: missing
- Status: escalated
- Attempt count: 3
- Attempt history:
  - Attempt 1: Validation Error: currency field required; posted_date invalid date or datetime, month value is outside expected range of 1-12
  - Attempt 2: Validation Error: currency field required; posted_date invalid date or datetime, month value is outside expected range of 1-12
  - Attempt 3: Validation Error: currency field required; posted_date invalid date or datetime, month value is outside expected range of 1-12
- Additional evidence: Please check ESCALATION.md for details.

Recommendation: Reject the payment settlement until the ledger entry is corrected and reconciliation can be completed successfully.

User decision: Please approve
