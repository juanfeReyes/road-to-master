# Pipeline CLI Contract

## Command

The package exposes a batch pipeline command alongside the existing `index` and `ask` commands:

```text
uv run l1-assistant pipeline [--questions PATH] [--output PATH] [--data-dir PATH] [--db-dir PATH]
```

## Arguments

- `--questions`: input CSV path. Defaults to `data/engineer_questions.csv`.
- `--output`: optional report CSV path. When omitted, create a timestamped report path.
- `--data-dir`: optional assistant source-document directory, matching the existing CLI behavior.
- `--db-dir`: optional local index directory, matching the existing CLI behavior.

## Success behavior

- Exit status `0`.
- Print a readable report containing every result row.
- Print the final report path.
- Write a stable report header and one row for each valid input question.

## Failure behavior

- Exit status non-zero.
- Write a clear diagnostic to standard error for missing/unreadable/malformed input, missing required header, blank/zero valid questions, or assistant initialization failure.
- A row-level assistant failure is represented in the report's `error` field and does not remove the row or prevent later rows from being processed.

## Timestamp rule

The default output filename includes a local, filesystem-safe timestamp with microsecond precision. An explicit `--output` path takes precedence and is not timestamp-mutated.
