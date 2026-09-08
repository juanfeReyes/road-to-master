# Quickstart: Agent Evaluation Pipeline

## Prerequisites

1. Provision the existing local embedding and generator model artifacts.
2. Provision a local judge model supported by the DeepEval adapter.
3. Build the local retrieval index:

```powershell
uv run l1-assistant index --data-dir ..\data --db-dir .\var\chroma
```

4. Prepare a JSONL dataset with at least one `id` and `input` per record. Add
   `expected_output` for correctness, contextual precision, and contextual recall,
   and add `expected_sources` for deterministic source validation.

## Run an evaluation

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --metrics generator,retrieval `
  --judge-model $env:L1_JUDGE_MODEL `
  --trace
```

The command must complete with no network access when all local artifacts are
available. Inspect the console aggregates and the JSON report. Confirm that the
report preserves question order, retrieval rank/source IDs, metric denominators,
judge reasons, and any partial failures.

## Validation scenarios

1. **Supported answer**: a question with a known answer and source should produce
   non-null generator and retrieval metrics.
2. **Unanswerable question**: an unsupported question should retain an explicit
   abstention answer and should not pass the non-empty/citation checks by default.
3. **Missing reference**: a record without `expected_output` should mark
   reference-dependent metrics unavailable while still evaluating answer
   relevancy, faithfulness, and contextual relevancy where eligible.
4. **Row failure**: force one local model/retriever failure and confirm later rows
   remain in the report with an actionable error and partial-run status.
5. **Offline boundary**: deny network access and confirm the same command succeeds
   with local artifacts or fails clearly with a missing-resource error; it must not
   silently contact hosted evaluation services.

## Tests

Run the existing focused tests from the package:

```powershell
uv run pytest tests/unit/test_evaluation.py tests/unit/test_pipeline.py tests/integration/test_evaluation_cli.py
```

Use a fake judge and fake retriever in automated tests so metric aggregation,
eligibility, report schema, and failure handling are deterministic. Reserve a
small explicitly marked local-model integration test for adapter wiring and
offline behavior.

See [evaluation-cli.md](contracts/evaluation-cli.md) and
[data-model.md](data-model.md) for the input and output contracts.
