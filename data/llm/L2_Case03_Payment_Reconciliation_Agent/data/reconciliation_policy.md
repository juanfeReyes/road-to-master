# Kestrel Payments - Reconciliation Policy (Synthetic)

> Owned by Finance Operations. Rina Okafor is the reconciliation lead and the
> escalation destination. Month-end close depends on this running clean.

**Reconciliation period: March 2026.** Treat 2026-04-05 as today.

## 1. Matching

Match a processor payment to a ledger entry on `order_ref`. A match is
**reconciled** when the amounts agree within tolerance and the currencies are
identical.

## 2. Amount tolerance

Amounts may differ by up to **0.02** in absolute terms, to absorb rounding
between the processor's and our own calculation. A difference of more than 0.02
is an exception and must be reported, never silently accepted.

## 3. Timing

The processor settles on the transaction date; our ledger posts on the banking
day. A ledger entry posting **within 3 calendar days** of settlement is timing,
not a discrepancy - including across a month boundary. Do not report these as
exceptions; month-end would be unreadable if you did.

## 4. Exception classes to report

- **Amount mismatch** beyond tolerance.
- **Currency mismatch** between payment and ledger entry.
- **Unmatched payment** - a payment with no ledger entry.
- **Duplicate settlement** - more than one payment against the same order where
  only one is expected.

Refunds appear as negative payments with a matching `refund` ledger entry. They
are normal and reconcile like any other entry.

## 5. Human validation gate

No exception is written to the close file without Rina's approval. The agent
prepares the exception with its evidence and reasoning; she accepts or rejects.
This applies to every exception class, because a wrongly reported exception costs
her as much time as a missed one.

## 6. When the ledger service misbehaves

The ledger API is maintained by another team and is not always well behaved. Your
agent must not treat a bad response as a fact about the business.

- Validate every response against the shape you expect **before** using it.
  A response that parses as JSON is not necessarily a response you can trust.
- On a failed or invalid response, retry with the specific reason for the
  failure included in the retry, so the next attempt has a chance of differing
  from the last. Retrying the identical call and hoping is not recovery.
- **After 3 failed attempts on the same record, stop and escalate.** Write the
  record, what you tried, what came back each time, and what you would need in
  order to proceed, to `ESCALATION.md`. Do not loop, and do not guess a value in
  order to keep going.
- A record you could not verify is not a reconciled record. Never mark it clean
  to complete the run.

## 7. Output

A reconciliation report: reconciled count, exceptions by class with their
evidence, records awaiting Rina's approval, and records escalated. The totals
must add up to the number of payments in the input - a record that quietly
disappears from the report is the worst outcome here, worse than a wrong
classification, because nobody knows to look for it.
