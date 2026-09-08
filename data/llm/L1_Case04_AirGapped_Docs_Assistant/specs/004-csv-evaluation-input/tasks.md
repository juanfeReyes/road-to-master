---

description: "Implementation tasks for CSV evaluation input"
---

# Tasks: CSV Evaluation Input

**Input**: Design documents from `specs/004-csv-evaluation-input/`

**Prerequisites**: `plan.md`, `spec.md`, `research.md`, `data-model.md`, `contracts/csv-evaluation-cli.md`, `quickstart.md`

**Tests**: Included because the feature specification defines independent test criteria and measurable acceptance scenarios.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Establish fixtures and test seams for the CSV evaluation feature without changing runtime behavior.

- [x] T001 [P] Add a representative valid CSV fixture with required and optional evaluation columns in `l1-assistant/tests/fixtures/evaluation.csv`
- [x] T002 [P] Add CSV edge-case fixtures covering BOM, quoted commas, embedded newlines, blank references, and extra columns in `l1-assistant/tests/fixtures/evaluation-csv-edge-cases.csv`
- [x] T003 [P] Add invalid CSV fixtures for missing headers, blank questions, duplicate IDs, malformed rows, empty files, and missing IDs in `l1-assistant/tests/fixtures/`
- [x] T004 [P] Review existing evaluation test helpers and expose reusable temporary-dataset setup in `l1-assistant/tests/conftest.py`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Define shared input normalization and error semantics before implementing story-specific behavior.

- [x] T005 [P] Add a typed dataset-format/provenance representation or equivalent constants for `csv` and `jsonl` in `l1-assistant/src/l1_assistant/models.py`
- [x] T006 [P] Define actionable CSV validation errors carrying row/line and field context in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T007 [P] Refactor evaluation dataset loading boundaries in `l1-assistant/src/l1_assistant/pipeline.py` so CSV and JSONL adapters return the existing `EvaluationRecord` shape
- [x] T008 [P] Remove temporary `print(records)` and `print(results)` debugging output from `l1-assistant/src/l1_assistant/cli.py`
- [x] T009 [P] Add focused unit-test scaffolding for format dispatch, raw-byte hashing, and normalized-record comparison in `l1-assistant/tests/unit/test_pipeline.py`

**Checkpoint**: Shared record, validation, and provenance boundaries are ready; user-story implementation can proceed.

---

## Phase 3: User Story 1 - Evaluate Questions from a CSV (Priority: P1) 🎯 MVP

**Goal**: Read valid CSV question rows, map them to the existing DeepEval records, and evaluate every row in order with the existing generator/retrieval pipeline.

**Independent Test**: Run the evaluation loader/evaluator against a valid CSV and verify that all rows remain ordered, references reach the normalized records, and the resulting report has one result per input row.

### Tests for User Story 1

- [x] T010 [P] [US1] Test valid CSV parsing preserves row order, question text, IDs, expected answers, and semicolon-delimited sources in `l1-assistant/tests/unit/test_pipeline.py`
- [x] T011 [P] [US1] Test quoted commas, escaped quotes, embedded newlines, UTF-8 text, and BOM headers in `l1-assistant/tests/unit/test_pipeline.py`
- [x] T012 [P] [US1] Test CSV records normalize to the same `EvaluationRecord` values as equivalent JSONL records in `l1-assistant/tests/unit/test_pipeline.py`
- [x] T013 [P] [US1] Test a valid CSV evaluation produces one ordered result per row and shared metric/report fields in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 1

- [x] T014 [US1] Implement BOM-safe, newline-safe `csv.DictReader` loading with required-header validation in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T015 [US1] Implement CSV row normalization for `question_id`, `engineer_question`, `expected_output`, and ordered deduplicated `expected_sources` in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T016 [US1] Add `.csv`/`.jsonl` dataset suffix dispatch while preserving the existing JSONL loader behavior in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T017 [US1] Route normalized CSV records through existing retrieval, LangChain answering, DeepEval metrics, aggregation, and row-result error handling in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T018 [US1] Extend evaluation report dataset provenance with source format, raw input hash, and normalized row count in `l1-assistant/src/l1_assistant/cli.py` and `l1-assistant/src/l1_assistant/models.py`

**Checkpoint**: Explicit valid CSV evaluation works independently and produces the standard DeepEval report shape.

---

## Phase 4: User Story 2 - Handle CSV Validation and Compatibility (Priority: P1)

**Goal**: Reject invalid CSV datasets clearly before claiming success, preserve all supported CSV field values, and avoid regressions in JSONL and legacy CSV workflows.

**Independent Test**: Run the loader/CLI against each invalid fixture and verify deterministic non-success errors; then run existing JSONL and legacy pipeline tests unchanged.

### Tests for User Story 2

- [x] T019 [P] [US2] Test missing/unreadable/empty CSV files return actionable non-success errors in `l1-assistant/tests/unit/test_pipeline.py`
- [x] T020 [P] [US2] Test missing required headers, blank questions, missing IDs, duplicate IDs, malformed rows, and invalid field shapes in `l1-assistant/tests/unit/test_pipeline.py`
- [x] T021 [P] [US2] Test missing expected references make only dependent metrics unavailable while independent metrics remain eligible in `l1-assistant/tests/unit/test_evaluation.py`
- [x] T022 [P] [US2] Test row-level assistant/judge failures retain ID and question and do not discard later rows in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [x] T023 [P] [US2] Add regression coverage proving existing JSONL loading and legacy `pipeline --questions` CSV scoring remain unchanged in `l1-assistant/tests/integration/`

### Implementation for User Story 2

- [x] T024 [US2] Add preflight validation for empty files, required columns, blank questions, missing IDs, duplicate IDs, malformed rows, and unreadable paths in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T025 [US2] Ensure CSV validation failures include stable row/line and field details and propagate to CLI stderr with a non-zero exit in `l1-assistant/src/l1_assistant/cli.py`
- [x] T026 [US2] Preserve optional-reference eligibility and explicit unavailable reasons for CSV records through existing DeepEval evaluation logic in `l1-assistant/src/l1_assistant/evaluation.py`
- [x] T027 [US2] Preserve row-level evaluation exceptions as failed results while continuing iteration over remaining normalized CSV records in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T028 [US2] Keep the legacy `load_questions` CSV path and its `pipeline` command separate from the new evaluation CSV adapter in `l1-assistant/src/l1_assistant/pipeline.py`

**Checkpoint**: Invalid CSV input fails safely and existing JSONL/legacy behavior remains covered.

---

## Phase 5: User Story 3 - Configure CSV Evaluation from the CLI (Priority: P2)

**Goal**: Support explicit CSV paths and the documented data-folder default while applying all existing evaluation options consistently.

**Independent Test**: Invoke `evaluate` once with an explicit CSV and once without `--dataset` when `data/engineer_questions.csv` exists; verify equivalent configuration and report structure.

### Tests for User Story 3

- [x] T029 [P] [US3] Test the evaluate parser accepts an omitted dataset and retains output, metrics, judge-model, threshold, max-question, and trace options in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [x] T030 [P] [US3] Test explicit CSV and default CSV resolution select the same dataset and preserve input limits/order in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [x] T031 [P] [US3] Test unsupported extensions and missing default CSV paths return clear non-success results in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 3

- [x] T032 [US3] Make `--dataset` optional for `evaluate` and resolve `data/engineer_questions.csv` under the configured data directory in `l1-assistant/src/l1_assistant/cli.py`
- [x] T033 [US3] Preserve all existing evaluate options and apply `--max-questions` after CSV normalization without evaluating rows beyond the limit in `l1-assistant/src/l1_assistant/pipeline.py`
- [x] T034 [US3] Add explicit unsupported-extension errors and ensure default/explicit paths are represented in report provenance in `l1-assistant/src/l1_assistant/cli.py`
- [x] T035 [US3] Document explicit and default CSV commands, schema, validation, and option compatibility in `l1-assistant/README.md`

**Checkpoint**: Explicit and default CLI CSV workflows are independently usable with the existing evaluation controls.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Validate the feature end-to-end and keep documentation, performance, and compatibility aligned.

- [x] T036 [P] Update `specs/004-csv-evaluation-input/quickstart.md` if implementation details or command defaults differ from the final CLI behavior
- [x] T037 [P] Update `specs/004-csv-evaluation-input/contracts/csv-evaluation-cli.md` with final error, provenance, and default-path behavior
- [x] T038 [P] Add a representative 100-row CSV test or benchmark proving order and row coverage meet the feature success criteria in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [x] T039 Run targeted CSV, JSONL, DeepEval eligibility, and legacy pipeline tests with `uv run pytest` from `l1-assistant/`
- [x] T040 Run the complete project test suite and Python compilation checks, then resolve only regressions caused by this feature
- [x] T041 Run all validation scenarios from `specs/004-csv-evaluation-input/quickstart.md` and record final command/report behavior in the implementation review

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No implementation dependency; fixture and test-seam tasks can begin immediately.
- **Foundational (Phase 2)**: Depends on Setup; blocks all user-story implementation.
- **User Story 1 (Phase 3)**: Depends on Foundational and delivers the MVP.
- **User Story 2 (Phase 4)**: Depends on the shared loader from US1; hardens validation and compatibility.
- **User Story 3 (Phase 5)**: Depends on the normalized loader from US1 and validation semantics from US2.
- **Polish (Phase 6)**: Depends on all required stories being complete.

### User Story Dependencies

- **US1 (P1)**: Can start after Phase 2; no dependency on another user story.
- **US2 (P1)**: Depends on US1's CSV adapter and shared record path; independently testable after those are complete.
- **US3 (P2)**: Depends on US1's format dispatch and US2's validation/error behavior; independently testable through the CLI.

### Parallel Opportunities

- Phase 1 tasks T001-T004 can run in parallel.
- Foundational tasks T005-T006 and T009 can run in parallel; T007-T008 follow the shared design review.
- US1 tests T010-T013 can run in parallel before implementation; T014-T015 can then proceed in parallel with T018 once the shared model contract is fixed.
- US2 tests T019-T023 can run in parallel; validation, eligibility, and legacy-path changes touch separate logical areas.
- US3 tests T029-T031 can run in parallel; documentation tasks T035-T037 can run alongside implementation review.

## Parallel Example: User Story 1

```text
Task: "Test CSV parsing and normalization in l1-assistant/tests/unit/test_pipeline.py"
Task: "Test CSV report cardinality in l1-assistant/tests/integration/test_evaluation_cli.py"
Task: "Implement CSV DictReader and row normalization in l1-assistant/src/l1_assistant/pipeline.py"
```

## Parallel Example: User Story 2

```text
Task: "Add invalid CSV fixture and preflight validation tests in l1-assistant/tests/unit/test_pipeline.py"
Task: "Add reference eligibility tests in l1-assistant/tests/unit/test_evaluation.py"
Task: "Add row-failure continuation tests in l1-assistant/tests/integration/test_evaluation_cli.py"
```

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1 and Phase 2.
2. Implement US1 explicit CSV loading, normalization, evaluation reuse, and report provenance.
3. Run US1 targeted tests and stop for independent validation.

### Incremental Delivery

1. Add US2 validation and compatibility hardening.
2. Add US3 default-path and CLI configuration support.
3. Complete documentation, 100-row coverage, full regression tests, and quickstart validation.

### Format Validation

All 41 tasks use the required checklist format: checkbox, sequential task ID,
optional `[P]`, required story label in story phases, and an explicit file path.
