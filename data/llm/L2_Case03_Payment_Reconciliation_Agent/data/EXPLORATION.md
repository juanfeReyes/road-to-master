
# Exploration

Following is the analisis of:
- API response: Based on ledger_api.py logic to know what will be the responses
- Expected Result: Is the manual analysis based the API Response, internal_ledged.jsonl and the processor_payments.csv
- Actual result is the outcome of the agent after processing each processor_payment.csv

| Payment ID | Order ID  |API Response| Expected Result           | Actual Result        |
|------------|-----------|---|---------------------------|----------------------|
| PAY-4001   | ORD-70001 |OK|   RECONCILED              | RECONCILED           |
| PAY-4002   | ORD-70002 |OK|   RECONCILED              | RECONCILED           |
| PAY-4003   | ORD-70003 |OK (Amount as string)|   RECONCILED              | RECONCILED           |
| PAY-4004   | ORD-70004 |OK|   RECONCILED              | RECONCILED           |
| PAY-4005   | ORD-70005 |OK (Amount as null)|   ESCALATED (amount: null)| ESCALATED            |
| PAY-4006   | ORD-70006 |OK|   RECONCILED              | RECONCILED           |
| PAY-4007   | ORD-70007 |OK|   RECONCILED              | RECONCILED           |
| PAY-4008   | ORD-70008 |OK| Amount mismatch           | Amount mismatch      |
| PAY-4009   | ORD-70009 |OK (Nested Schema)| Amount mismatch           | Amount mismatch      |
| PAY-4010   | ORD-70010 |OK|   RECONCILED              | RECONCILED           |
| PAY-4011   | ORD-70011 |OK| Duplicate settlement      | Duplicate settlement |
| PAY-4012   | ORD-70012 |OK| Unmatched payment         | Unmatched payment    |
| PAY-4013   | ORD-70013 |OK (Not found) | Unmatched payment         | Duplicate settlement    |
| PAY-4014   | ORD-70014 |OK|   RECONCILED              | RECONCILED           |
| PAY-4015   | ORD-70015 |OK|   RECONCILED              | RECONCILED           |
| PAY-4016   | ORD-70016 |OK (Different Date fortmat)| Currency mismatch         | Currency mismatch    |
| PAY-4017   | ORD-70017 |OK (Absolute amount)|   RECONCILED (refund)         | Reconciled      |
| PAY-4018   | ORD-70018 |OK|   RECONCILED              | RECONCILED           |
| PAY-4019   | ORD-70019 |OK (Amount as string)|   RECONCILED              | RECONCILED           |
| PAY-4020   | ORD-70020 |OK (After 2 attempts)|   RECONCILED              | RECONCILED           |
| PAY-4021   | ORD-70021 |OK|   RECONCILED              | RECONCILED           |
| PAY-4022   | ORD-70022 |OK (Nested Schema)|   ESCALATED (schema: v2)  | ESCALATED            |
| PAY-4023   | ORD-70023 |500 (Timeout)|   ESCALATED (rate-limit)  | ESCALATED            |
| PAY-4024   | ORD-70024 |OK|   RECONCILED              | RECONCILED           |
