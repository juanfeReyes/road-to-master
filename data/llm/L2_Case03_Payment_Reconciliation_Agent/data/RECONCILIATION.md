
```markdown
# Payment Settlement

- **Title:** PAY-4001 - Payment settlement summary
- **Reasoning:** Payment PAY-4001 reconciles cleanly with ledger entry FAKE-4001 for order ORD-70001. Amount and currency match exactly, settlement and posting dates align on 2026-03-02, and only one attempt was needed, indicating a straightforward settlement.
- **Exception:** None
- **Evidence:** Matched on order_ref ORD-70001; payment and ledger both show 249.99 GBP; same-day settlement and posting; status is reconciled.
- **Recommendation:** Approved.
- **Status:** approved
- **User Answer:** Approve please
```

## Payment Settlement

- **Title:** PAY-4002 - Payment settlement summary
- **Reasoning:** The payment matches the ledger entry on order reference, amount, currency, and settled date, so the core settlement details align correctly. However, settlement history already shows another settled payment for the same order, indicating this transaction is not unique.
- **Exception:** Duplicate settlement
- **Evidence:** PAY-4002 matches ledger entry FAKE-4001 for ORD-70001, GBP 249.99, posted on 2026-03-02. Existing settled payment PAY-4001 is already recorded for the same order.
- **Recommendation:** The payment settlement should be rejected.
- **Status:** awaiting_approval
- **User Answer:** Reject
