# Implementation Plan: Batch Question Scoring Pipeline

**Branch**: `002-batch-question-scoring` | **Date**: 2026-09-04 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from [spec.md](./spec.md)

## Summary

Add a Python pipeline module and CLI entry point under the existing `l1-assistant/src/l1_assistant` package. It will read the `question_id,engineer_question` CSV format, evaluate each non-blank question through the existing answering/retrieval components, print an ordered report, and write a timestamped CSV report so repeated executions do not overwrite one another by default.

## Technical Context

- **Language/Version**: Python 3.14 (project requirement)
- **Primary Dependencies**: Existing standard-library CSV/path/time functionality and `l1_assistant.answering.answer_question`; no new runtime dependencies
- **Storage**: Input and output CSV files; existing local Chroma index for retrieval
- **Testing**: Existing pytest suite; add focused unit tests for CSV parsing, timestamped output naming, report serialization, and row-level failure handling
- **Target Platform**: Offline local execution on the project-supported Windows/Linux environments
- **Project Type**: Python CLI package
- **Performance Goals**: Process 100 input rows sequentially without dropping rows; report generation overhead should remain negligible compared with model inference
- **Constraints**: Air-gapped/offline operation; preserve existing score semantics; do not overwrite a prior report by default; fail clearly for missing/malformed/empty input
- **Scale/Scope**: One CSV batch per invocation, with a representative target of 100 questions; sequential processing is intentional for predictable ordering and resource use

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The repository constitution is still the uninstantiated template, so it defines no enforceable project-specific gates. The plan follows the repository's observable conventions: keep the feature inside the existing Python package and CLI surface, reuse existing answering and score behavior, remain offline and dependency-free beyond the current project, and cover the new behavior with pytest.

**Gate status**: PASS; no constitution violation or complexity exception is required.

## Project Structure

### Documentation (this feature)

```text
specs/002-batch-question-scoring/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── pipeline-cli.md
└── tasks.md              # Phase 2 output (/speckit-tasks)
```

### Source Code (repository root)

```text
l1-assistant/
├── src/
│   └── l1_assistant/
│       ├── cli.py              # existing index/ask CLI plus pipeline command
│       ├── evaluation.py      # existing evaluation helper
│       └── pipeline.py         # CSV pipeline and timestamped report output
└── tests/
    ├── unit/
    │   ├── test_evaluation.py # pipeline-focused tests
    │   └── ...
    └── conftest.py
```

**Structure Decision**: Extend the existing `l1_assistant` package rather than creating a parallel top-level script. Keep reusable CSV/report functions separate from CLI argument parsing, and have the CLI invoke the same pipeline functions that unit tests exercise.

## Complexity Tracking

No violations. The implementation remains a single package and uses existing dependencies.
