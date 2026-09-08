# Quickstart: Batch Question Scoring Pipeline

## Prerequisites

1. Install the project dependencies with the repository's existing `uv` workflow.
2. Provision the configured local embedding/chat model artifacts.
3. Build the local document index:

```powershell
cd l1-assistant
uv run l1-assistant index --data-dir ..\data --db-dir .\var\chroma
```

## Run the default batch

From `l1-assistant/`:

```powershell
uv run l1-assistant pipeline `
  --questions ..\data\engineer_questions.csv `
  --data-dir ..\data `
  --db-dir .\var\chroma
```

Expected outcomes:

- The console prints one report row for each non-blank question in the input.
- The console prints the generated timestamped CSV path.
- The output CSV contains the columns and row semantics in [data-model.md](./data-model.md).
- Two runs without `--output` create two distinct timestamped report files, including when started within the same second.

## Run with an explicit output path

```powershell
uv run l1-assistant pipeline `
  --questions .\tests\fixtures\questions.csv `
  --output .\var\reports\questions-test.csv `
  --data-dir ..\data `
  --db-dir .\var\chroma
```

Use this form in automated tests when the output path must be deterministic.

## Validation scenarios

- Valid multi-row input: verify row count and input order.
- Missing input path: verify non-zero exit and a clear error.
- Missing `engineer_question` header: verify non-zero exit and no successful report.
- A row-level assistant failure: verify the row remains in the CSV with `error` populated and later rows are present.
- Two default runs: verify timestamped filenames differ and the first report remains intact.
