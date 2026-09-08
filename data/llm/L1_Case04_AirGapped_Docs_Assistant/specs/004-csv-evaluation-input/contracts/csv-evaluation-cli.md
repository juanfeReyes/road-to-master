# CSV Evaluation CLI Contract

The existing `l1-assistant evaluate` command accepts both JSONL and CSV datasets.
Existing `index`, `ask`, and legacy `pipeline` behavior remains unchanged.

## Commands

Explicit CSV:

```powershell
uv run l1-assistant evaluate `
  --dataset ..\data\engineer_questions.csv `
  --output .\var\reports\evaluation.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --metrics generator,retrieval `
  --judge-model $env:L1_JUDGE_MODEL
```

Default CSV:

```powershell
uv run l1-assistant evaluate `
  --output .\var\reports\evaluation.json `
  --data-dir ..\data `
  --db-dir .\var\chroma
```

When omitted, `--dataset` resolves to `data/engineer_questions.csv` under the
configured data directory.

## CSV input

The file is UTF-8 or UTF-8 with BOM, comma-delimited, and header-based:

```csv
question_id,engineer_question,expected_output,expected_sources
Q01,"What checks are required?","Isolate and depressurize first.","maintenance.md;safety.md"
```

Required columns:

- `question_id`
- `engineer_question`

Optional columns:

- `expected_output`
- `expected_sources` (semicolon-delimited)

Quoted commas, quotes, and embedded newlines are supported. Unknown additional
columns are ignored after required-column validation. Missing/duplicate IDs,
blank questions, malformed CSV, and missing required headers produce a non-zero
status before evaluation.

## Evaluation behavior

CSV rows normalize to the same records as JSONL. All existing options apply:
`--metrics`, `--judge-model`, `--max-questions`, `--threshold`, `--trace`, and
`--output`. Missing expected answers make reference-dependent DeepEval metrics
unavailable with a reason; reference-independent metrics still run.

The report contains the standard metric definitions, aggregates, denominators,
ordered results, row-level failures, and dataset provenance including `format=csv`.
Rows that fail during assistant or judge evaluation remain in the report, and
later rows continue processing.
