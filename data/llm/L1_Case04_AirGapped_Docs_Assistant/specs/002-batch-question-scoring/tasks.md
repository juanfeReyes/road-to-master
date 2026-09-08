# Tasks: Batch Question Scoring Pipeline

**Input**: Design documents from `/specs/002-batch-question-scoring/`

**Prerequisites**: [plan.md](./plan.md), [spec.md](./spec.md), [research.md](./research.md), [data-model.md](./data-model.md), [contracts/pipeline-cli.md](./contracts/pipeline-cli.md), [quickstart.md](./quickstart.md)

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Establish the package locations and execution assumptions required by the pipeline.

- [X] T001 Confirm the existing `l1-assistant` package entry point and CLI command registration in `l1-assistant/pyproject.toml` and `l1-assistant/src/l1_assistant/__init__.py`
- [X] T002 [P] Confirm the default input dataset schema and report output directory conventions from `data/engineer_questions.csv` and the project README

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Define shared report data, CSV validation, and timestamped output behavior before story-specific CLI wiring.

- [X] T003 Create reusable question-row and evaluation-result types in `l1-assistant/src/l1_assistant/pipeline.py` based on `specs/002-batch-question-scoring/data-model.md`
- [X] T004 Implement CSV input loading and validation for `question_id` and `engineer_question` in `l1-assistant/src/l1_assistant/pipeline.py`, including missing-file, malformed-file, missing-header, blank-row, and empty-input errors
- [X] T005 Implement default timestamped report-path generation with microsecond precision and explicit output-path handling in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T006 Implement stable CSV serialization with the required header and safe handling of empty scores and multiline text in `l1-assistant/src/l1_assistant/pipeline.py`

**Checkpoint**: Shared pipeline data and file behavior are ready; user story work can begin.

---

## Phase 3: User Story 1 - Score a Question Dataset (Priority: P1) 🎯 MVP

**Goal**: Evaluate every valid input question exactly once, preserve input order, and retain a result for every row.

**Independent Test**: Run the pipeline against a multi-row fixture with a mocked answering flow and verify one ordered result per valid input row.

### Implementation for User Story 1

- [X] T007 [US1] Implement sequential question evaluation in `l1-assistant/src/l1_assistant/pipeline.py` by reusing `answer_question` and the configured `LocalRetriever`
- [X] T008 [US1] Preserve `id`, question text, answer, sources, score, and score explanation in each result in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T009 [US1] Capture expected row-level evaluation failures in the result `error` field while continuing to later rows in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T010 [US1] Add the `pipeline` subcommand and its `--questions`, `--output`, `--data-dir`, and `--db-dir` arguments in `l1-assistant/src/l1_assistant/cli.py`
- [X] T011 [US1] Wire CLI pipeline execution to validate settings, initialize the retriever, evaluate all rows, and return a non-zero result for fatal input or initialization failures in `l1-assistant/src/l1_assistant/cli.py`

**Checkpoint**: User Story 1 provides a complete ordered batch evaluation through the CLI.

---

## Phase 4: User Story 2 - Review Results in the Console (Priority: P2)

**Goal**: Print every evaluation result in a readable report and expose row-level failures without hiding successful rows.

**Independent Test**: Run the command with successful and failing mocked rows and verify that the console includes all rows, scores, and failure details.

### Implementation for User Story 2

- [X] T012 [US2] Implement readable report formatting for question ID, question, score, answer, sources, score explanation, and error in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T013 [US2] Print the complete ordered report and summary/output-path information from the `pipeline` command in `l1-assistant/src/l1_assistant/cli.py`
- [X] T014 [US2] Ensure console failures are written to standard error with a non-zero exit status while row-level failures remain visible in standard report output in `l1-assistant/src/l1_assistant/cli.py`

**Checkpoint**: User Story 2 provides an inspectable console report for complete and partial batches.

---

## Phase 5: User Story 3 - Save Results for Later Analysis (Priority: P3)

**Goal**: Persist successful batch reports as timestamped CSV files while supporting deterministic explicit paths.

**Independent Test**: Run the pipeline twice without `--output` and verify distinct timestamped files; run once with an explicit path and verify the stable CSV header and row count.

### Implementation for User Story 3

- [X] T015 [US3] Write the fully evaluated report only after processing completes successfully in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T016 [US3] Create parent directories for configured report paths and surface write errors without claiming that a report was saved in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T017 [US3] Ensure default report names include a local microsecond timestamp and explicit `--output` paths are not timestamp-mutated in `l1-assistant/src/l1_assistant/cli.py`

**Checkpoint**: User Story 3 produces durable, repeatable CSV reports without default overwrites.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Validate the integrated feature and keep user-facing documentation aligned.

- [X] T018 [P] Update `l1-assistant/README.md` with the pipeline command, default dataset, timestamped output behavior, and explicit output-path example
- [X] T019 Run the existing pytest suite from `l1-assistant/` with `uv run pytest` and resolve regressions caused by the CLI/pipeline integration
- [X] T020 Run every validation scenario in `specs/002-batch-question-scoring/quickstart.md`, including two same-second default executions and malformed-input failure behavior

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies; can start immediately.
- **Foundational (Phase 2)**: Depends on Setup; blocks all user stories.
- **User Stories (Phases 3-5)**: Depend on Foundational completion.
- **Polish (Phase 6)**: Depends on all desired user stories being complete.

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Phase 2; no dependency on other stories. This is the MVP.
- **User Story 2 (P2)**: Depends on the evaluation flow from US1, then adds console presentation.
- **User Story 3 (P3)**: Depends on the result model and evaluation flow from US1; output persistence can be validated independently with explicit paths.

### Parallel Opportunities

- T002 can run in parallel with T001.
- T003, T004, T005, and T006 touch the same new module and should be implemented as one coordinated foundational workstream rather than concurrently edited.
- After Phase 2, US2 and US3 can be developed in parallel with US1 only if the shared pipeline interfaces are agreed first; the recommended execution order remains P1 → P2 → P3.
- T018 can run in parallel with final implementation review after the CLI contract is stable.

## Implementation Strategy

1. Complete the shared pipeline model and file contract.
2. Deliver User Story 1 as the MVP: evaluate all rows in order through the existing assistant.
3. Add console rendering for User Story 2.
4. Add timestamped and explicit-path persistence for User Story 3.
5. Run the existing suite and quickstart validation before completion.

## Format Validation

All implementation tasks use the required `- [ ] [TaskID] [P?] [Story?] description` checklist format, include concrete file paths, and use story labels only in user-story phases.
