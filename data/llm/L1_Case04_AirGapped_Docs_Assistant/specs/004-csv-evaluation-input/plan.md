# Implementation Plan: CSV Evaluation Input

**Branch**: `004-csv-evaluation-input` | **Date**: 2026-09-06 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from [spec.md](./spec.md)

## Summary

Extend the existing DeepEval/LangChain evaluation workflow with a CSV input
adapter. The adapter will detect `.csv` datasets, parse them with Python's
standard CSV reader using UTF-8 BOM tolerance and newline-safe handling, validate
the required `engineer_question` and unique `question_id` fields, normalize
optional answer/source references into the existing `EvaluationRecord`, and then
reuse the current DeepEval metrics, local LangChain judge, tracing, aggregation,
and JSON report path. Existing JSONL behavior and the older CSV scoring pipeline
will remain compatible.

## Technical Context

- **Language/Version**: Python 3.14 (project requirement)
- **Primary Dependencies**: Existing standard-library `csv`, `hashlib`, and
  `pathlib`; existing DeepEval and LangChain integrations; no new dependency
- **Storage**: CSV input under the configured data directory; existing JSON
  evaluation report and local retrieval index
- **Testing**: Existing pytest suite; CSV parser unit tests, equivalence tests
  against JSONL, CLI integration tests, and regression tests for old CSV pipeline
- **Target Platform**: Offline local execution on supported Windows/Linux systems
- **Project Type**: Python CLI package
- **Performance Goals**: Stream rows without loading unnecessary intermediate
  representations; process at least 100 valid CSV questions while preserving order
- **Constraints**: UTF-8/UTF-8-BOM, comma-delimited header CSV; quoted commas and
  embedded newlines must work; no pandas dependency; retain row-level failures;
  no hosted DeepEval calls
- **Scale/Scope**: One CSV or JSONL dataset per evaluation invocation, existing
  `max_questions` limit, same DeepEval metric groups and report schema

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The repository constitution is an uninstantiated template and provides no
enforceable project-specific gates. The plan follows repository conventions:
it keeps CSV support in the existing package, uses the standard library, reuses
the existing normalized evaluation and DeepEval paths, preserves offline
operation, and adds focused pytest coverage.

**Gate status**: PASS; no constitution violation or complexity exception is required.

## Project Structure

### Documentation (this feature)

```text
specs/004-csv-evaluation-input/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── csv-evaluation-cli.md
└── tasks.md              # Phase 2 output (/speckit-tasks)
```

### Source Code (repository root)

```text
l1-assistant/
├── src/
│   └── l1_assistant/
│       ├── cli.py                  # dataset default and format dispatch
│       ├── pipeline.py             # CSV loader and shared orchestration
│       ├── models.py               # existing EvaluationRecord contract
│       └── evaluation.py           # existing DeepEval metric path
└── tests/
    ├── fixtures/
    │   ├── evaluation.csv
    │   ├── evaluation-csv-edge-cases.csv
    │   └── evaluation.jsonl
    ├── unit/
    │   └── test_pipeline.py        # CSV parser and normalization tests
    └── integration/
        └── test_evaluation_cli.py  # CLI dispatch and report equivalence
```

**Structure Decision**: Add a focused CSV loader beside the existing JSONL loader
in `pipeline.py`; both return `EvaluationRecord` objects. Keep the current
DeepEval metric construction unchanged so input-format support cannot diverge
metric semantics or report schema. Add only CLI format/default handling and tests.

## Design Decisions

1. Parse CSV with `csv.DictReader`, `encoding="utf-8-sig"`, and `newline=""`.
   This preserves quoted commas, embedded newlines, and optional BOMs.
2. Require `engineer_question` and `question_id` for evaluation CSVs. Normalize
   `expected_output` to `None` when missing/blank and split
   `expected_sources` on semicolons, trimming and deduplicating in order.
3. Dispatch based on the `.csv` or `.jsonl` suffix. Keep JSONL loading unchanged;
   do not convert CSV through a temporary JSONL file.
4. Reuse `EvaluationRecord`, `build_test_case`, deterministic source checks,
   DeepEval metrics, aggregate denominators, tracing, and `EvaluationReport`.
5. Use `data/engineer_questions.csv` as the default when `--dataset` is omitted,
   while preserving explicit paths and all existing evaluation options.
6. Hash raw input bytes for provenance so CSV and JSONL reports identify their
   actual source content even when normalized records are equivalent.
7. Treat malformed structure and invalid required fields as preflight errors.
   Treat assistant/judge failures as row-level results and continue with later
   rows, matching existing evaluation behavior.

## Complexity Tracking

No violations. A dedicated adapter is simpler and safer than changing the
existing CSV scoring model or adding a dataframe dependency; shared normalization
avoids duplicating DeepEval and LangChain integration logic.
