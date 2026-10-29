# Submission Checklist — L2_Case03_Payment_Reconciliation_Agent

## Deliverables

- [ ] Working agent that processes all 24 payments and produces a reconciliation report.
- [ ] **Report totals that add up to 24.** Reconciled + exceptions + awaiting approval + escalated must account for every input payment.
- [ ] Exceptions classified per the policy, each with the evidence Rina needs to accept or reject.
- [ ] Your response-validation approach, and the list of ledger-service misbehaviours you found.
- [ ] `ESCALATION.md` as produced by an actual run, containing every attempt and its outcome.
- [ ] Evidence of the human approval gate genuinely halting, and what Rina sees.
- [ ] Passing `pytest-asyncio` output covering the agent loop, the ledger call with responses mocked, each recovery path, and escalation.
- [ ] Observability evidence sufficient to reconstruct any record's path.
- [ ] `REFLECTION.md`, 600-1000 words, six sections. The failure sections carry the most weight.
- [ ] Declared-effort statement: approximate hours and what you cut.

## Evidence standard

Every claim cites a payment id, an order reference, or a log line. "The agent
handles API errors" scores nothing. "ORD-70009 returns a nested envelope; pydantic
rejected it, the retry included the schema mismatch, and the record still appears
in the report as an amount_mismatch exception rather than being dropped" scores.

## Before you submit — challenge your own work

- [ ] **Do my totals add up to 24?** Check after adding error handling, not before — that is usually where records start disappearing.
- [ ] Did I call the stub API by hand for every order reference before writing the agent? What came back that I did not expect?
- [ ] For every record my agent could not verify: is it escalated, or did it end up marked reconciled to complete the run?
- [ ] Is there any path where my agent substitutes a default or a coerced value for something the ledger did not actually say?
- [ ] Does my retry include the reason the previous attempt failed, or does it just call again?
- [ ] Have I reported anything as an exception that the policy says is normal? The tolerance and timing rules exist to stop exactly that.
- [ ] Is there a response my agent accepts that it should not? What is the most wrong value I could return that would still pass my validation?
- [ ] Does my approval gate halt, or log a warning and continue?

## How this will be assessed

Your agent is run **unchanged** against a version of the ledger stub with
additional misbehaviours you have not seen. Validation fitted to the specific
failures you happened to find will not survive that; validation built against the
documented contract will.

Two outcomes are weighted above everything else: **a record that disappears from
the report**, and **a record marked reconciled that could not actually be
verified**. Either caps *Agentic Operations* regardless of the rest.

You will also answer several questions about your own submission at submission
time. They reference specific records and code paths, so they cannot be prepared
in advance.
