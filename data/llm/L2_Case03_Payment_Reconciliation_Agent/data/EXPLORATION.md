| Payment ID | Order ID  | Expected Result           | Actual Result        |
|------------|-----------|---------------------------|----------------------|
| PAY-4001   | ORD-70001 |   RECONCILED              | RECONCILED           |
| PAY-4002   | ORD-70002 |   RECONCILED              | RECONCILED           |
| PAY-4003   | ORD-70003 |   RECONCILED              | RECONCILED           |
| PAY-4004   | ORD-70004 |   RECONCILED              | RECONCILED           |
| PAY-4005   | ORD-70005 |   ESCALATED (amount: null)| ESCALATED            |
| PAY-4006   | ORD-70006 |   RECONCILED              | RECONCILED           |
| PAY-4007   | ORD-70007 |   RECONCILED              | RECONCILED           |
| PAY-4008   | ORD-70008 | Amount mismatch           | Amount mismatch      |
| PAY-4009   | ORD-70009 | Amount mismatch           | Amount mismatch      |
| PAY-4010   | ORD-70010 |   RECONCILED              | RECONCILED           |
| PAY-4011   | ORD-70011 | Duplicate settlement      | Duplicate settlement |
| PAY-4012   | ORD-70012 | Unmatched payment         | Unmatched payment    |
| PAY-4013   | ORD-70013 | Unmatched payment         | Unmatched payment    |
| PAY-4014   | ORD-70014 |   RECONCILED              | RECONCILED           |
| PAY-4015   | ORD-70015 |   RECONCILED              | RECONCILED           |
| PAY-4016   | ORD-70016 | Currency mismatch         | Currency mismatch    |
| PAY-4017   | ORD-70017 |   Amount mismatch         | Amount mismatch      |
| PAY-4018   | ORD-70018 |   RECONCILED              | RECONCILED           |
| PAY-4019   | ORD-70019 |   RECONCILED              | RECONCILED           |
| PAY-4020   | ORD-70020 |   RECONCILED              | RECONCILED           |
| PAY-4021   | ORD-70021 |   RECONCILED              | RECONCILED           |
| PAY-4022   | ORD-70022 |   ESCALATED (schema: v2)  | ESCALATED            |
| PAY-4023   | ORD-70023 |   ESCALATED (rate-limit)  | ESCALATED            |
| PAY-4024   | ORD-70024 |   RECONCILED              | RECONCILED           |
