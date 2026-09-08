# Quickstart: CSV Evaluation Input

## Prerequisites

1. Install the project environment and provision local embedding, generator, and
   DeepEval judge models.
2. Build the local index:

```powershell
uv run l1-assistant index --data-dir ..\data --db-dir .\var\chroma
```

3. Create `data/engineer_questions.csv` with the required
   `question_id,engineer_question` headers. Add `expected_output` and
   `expected_sources` when reference-dependent metrics are desired.

## Run with an explicit CSV

```powershell
uv run l1-assistant evaluate `
  --dataset ..\data\engineer_questions.csv `
  --output .\var\reports\csv-evaluation.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --metrics generator,retrieval `
  --judge-model $env:L1_JUDGE_MODEL
```

## Run with the default CSV

```powershell
uv run l1-assistant evaluate `
  --output .\var\reports\csv-default.json `
  --data-dir ..\data `
  --db-dir .\var\chroma
```

## Validation scenarios

1. A valid CSV produces one ordered report result per valid row.
2. A CSV with quoted commas, UTF-8 text, and an embedded newline preserves the
   normalized question and references.
3. A CSV without `expected_output` runs answer relevancy, faithfulness, and
   contextual relevancy while marking reference-dependent metrics unavailable.
4. A missing header, blank question, duplicate ID, malformed row, or missing file
   returns a non-zero status without writing a successful report.
5. A row-level assistant/judge failure appears in the report and later rows remain.
6. An equivalent JSONL and CSV dataset produce the same normalized records and
   report metric fields, aside from source format and raw hash.
7. Existing `.jsonl` evaluation and legacy `pipeline --questions` CSV behavior
   continue to pass their existing tests.

## Tests

```powershell
uv run pytest tests/unit/test_pipeline.py tests/integration/test_evaluation_cli.py
uv run pytest
```

See [csv-evaluation-cli.md](contracts/csv-evaluation-cli.md) and
[data-model.md](data-model.md) for the complete contract.
