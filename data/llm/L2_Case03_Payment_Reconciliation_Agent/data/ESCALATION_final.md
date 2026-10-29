
# PAY-4005 - Escalation report

## Error analysis
Payment **PAY-4005** for **GBP 312.75** on order **ORD-70005** could not be processed automatically after **3 attempts**. Each attempt failed with the same validation error: the ledger API response contained **`currency = None`**, while the system requires a valid string value for currency. This indicates a persistent data-quality or schema issue in the ledger entry (**LED-4005**) rather than a transient retry problem. There is also a likely reconciliation inconsistency: the provided evidence references **ORD-70004**, **amount 45.00**, and settlement date **2026-03-03**, which do not match this payment’s actual details (**ORD-70005**, **312.75**, **2026-03-04**). That suggests the evidence may have been attached from a different case.

## How to proceed next
To continue, I would need the ledger record for **LED-4005 / ORD-70005** to be corrected or reissued with a valid **currency** value. I would also need refreshed reconciliation evidence specifically for **PAY-4005**, confirming the correct order reference, amount, currency, and settlement/posting dates, so the payment can be reviewed and matched accurately.

# PAY-4009 - Escalation report

**Error analysis:** Payment PAY-4009 for ORD-70009 settled at 63.20 GBP on 2026-03-06, while the linked ledger entry LED-4009 shows 28.20 GBP, creating a 35.00 GBP discrepancy and supporting the current **Amount mismatch** exception. The attempt history shows two failure patterns across repeated retries: first, the ledger response returned `currency=None`, causing validation to fail; second, the parser expected a flat `LedgerApiResponse` schema, but the actual payload is nested under `data.entry` with `meta.schema=v2`. Even after valid ledger values appeared in the nested payload, validation still failed because the integration did not map the v2 structure correctly. This indicates both an upstream data-quality issue and a schema-mapping issue, with retries unable to resolve either automatically.

**How to proceed next:** To proceed, I would need confirmation of the authoritative ledger amount for ORD-70009, plus either a corrected flat ledger response or updated parsing logic for the `data.entry` v2 schema. I would also need confirmation whether the 35.00 GBP difference reflects a partial posting, split settlement, or an incorrect payment amount.

# PAY-4012 - Escalation report

**Error analysis:**  
Payment PAY-4012 for order ORD-70012 (£88 GBP, settled 2026-03-07) escalated after repeated ledger lookup and parsing failures. The attempt history shows three phases of issues: first, the ledger API returned an invalid payload with `currency=None`; second, it returned a nested `data.entry` schema that the validator could not map to the expected flat `LedgerApiResponse`; third, it consistently returned `{'error': 'not_found', 'order_ref': 'ORD-70012'}`, which again failed validation because the parser expected a full ledger entry. The current ledger result therefore indicates no ledger entry exists for ORD-70012. Although the exception class is marked **Duplicate settlement**, the supplied evidence appears inconsistent because it references different payments and order refs (ORD-70010, PAY-4010, PAY-4011, £199), not PAY-4012 / ORD-70012.

**How to proceed next:**  
To proceed, I would need corrected evidence specific to PAY-4012, confirmation whether ORD-70012 should exist in the ledger, and either a valid ledger response schema mapping or manual ledger lookup results to determine whether this is truly a duplicate settlement or a missing-ledger-entry case.

# PAY-4013 - Escalation report

**Error analysis:** Payment **PAY-4013** for **ORD-70013** (£510.45, transfer, settled 2026-03-08) was escalated after 3 failed attempts and flagged as **Duplicate settlement**. The current ledger lookup returned `not_found` for `ORD-70013`, but the attempt history shows a broader integration issue rather than a confirmed duplicate. Earlier retries failed because the ledger API response did not match the expected schema: first with `currency=None`, then with nested `data.entry` payloads that were not parsed correctly, and finally with `not_found` responses being validated as if they were successful ledger entries. This indicates the process is conflating API error payloads and schema-version differences with actual settlement validation. As a result, there is no reliable ledger confirmation for this payment yet.

**How to proceed next:** To proceed, I would need the contents of **ESCALATION.md**, the raw ledger API responses for `ORD-70013`, and confirmation of the expected response schema/error contract. A manual ledger search for both `payment_id` and `order_ref` is also needed to verify whether this is truly a duplicate settlement or a missing ledger posting.

# PAY-4016 - Escalation report

**Error analysis:** The payment is flagged for escalation because the reconciliation flow appears to have relied on inconsistent and likely unrelated ledger lookup attempts before reaching the current record. The attempt history shows repeated schema-validation failures: null `currency`, missing top-level fields when the API returned a nested `data.entry` payload, `not_found` responses for other order references, and finally a date parsing failure because `posted_date` was supplied as `09/03/2026` instead of an expected ISO format. The current payment data itself is also inconsistent: payment `PAY-4016` is for `ORD-70016`, `300 EUR`, while the evidence references `ORD-70015`, `132.1 GBP`, and a different settlement date. Although a ledger entry is present, it is `300 GBP`, creating a currency mismatch against the payment.

**How to proceed next:** To continue, I would need corrected reconciliation evidence tied specifically to `PAY-4016` / `ORD-70016`, confirmation of the authoritative currency, and a normalized ledger API response in the expected schema with ISO-formatted dates. Please also confirm whether the ledger amount should be converted or matched in source currency before approval.

# PAY-4022 - Escalation report

**Error analysis:**  
The payment is marked for escalation after 3 attempts, but the underlying issue appears to be a reconciliation mismatch rather than a payment exception. The current payment details show **PAY-4022 / ORD-70022 / GBP 2100**, while the evidence references a successful match against **ORD-70021** for **GBP 189.99** on **2026-03-11**. This indicates the matching logic likely attached evidence from a different case. The attempt history also shows repeated schema-validation failures when parsing ledger responses: earlier attempts failed on null `currency`, nested `v2` response structures not matching the expected flat model, `not_found` payloads, timeout payloads, and non-ISO date formatting. The provided ledger entry for PAY-4022 is valid in content, but wrapped in `data.entry`, which likely caused the parser failure.

**How to proceed next:**  
To proceed, I need the correct ledger lookup and reconciliation evidence specifically for **ORD-70022**, plus confirmation that the parser supports the **v2 nested ledger schema**. Please also confirm whether the intended match is **LED-4022** for **GBP 2100** posted on **2026-03-12**, and remove the unrelated ORD-70021 evidence before reprocessing.

# PAY-4023 - Escalation report

**Error analysis:** Payment PAY-4023 (£33.25 GBP, order_ref ORD-70023, settled 2026-03-12) escalated after 3 failed reconciliation attempts. Each attempt shows the ledger API returning `{'error': 'ledger service timeout for ORD-70023'}` rather than a valid ledger entry, which then triggered validation failures for all required fields (`entry_id`, `order_ref`, `amount`, `currency`, `posted_date`, `entry_type`). This indicates the primary issue is upstream ledger availability, not a confirmed payment mismatch. The current `ledger_entry` field repeats the same timeout error, and no exception class was recorded. Although the attached evidence mentions a likely schema/validation issue in another case, that reasoning appears inconsistent with this payment because PAY-4023 never received a usable ledger payload to compare.

**How to proceed next:** To continue, I would need either a successful ledger response for ORD-70023 or a manual ledger extract containing entry ID, order reference, amount, currency, posted date, and entry type. If the ledger service remains unstable, infra/support should confirm timeout logs and retry once service health is restored.
