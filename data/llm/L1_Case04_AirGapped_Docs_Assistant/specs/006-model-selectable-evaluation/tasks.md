# Tasks: Model-Selectable Evaluation

**Input**: Design documents from `/specs/006-model-selectable-evaluation/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are included because the feature spec and quickstart require unit, integration, and regression validation.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Prepare dependencies and documentation for model-source selection work.

- [X] T001 Add Portkey-compatible evaluation dependencies in `l1-assistant/pyproject.toml`
- [X] T002 Update evaluation usage overview for model-source selection in `l1-assistant/README.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Establish shared runtime model configuration and validation primitives required by all user stories.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T003 Create runtime model-source data structures in `l1-assistant/src/l1_assistant/models.py`
- [X] T004 [P] Add environment-backed defaults for local and Portkey settings in `l1-assistant/src/l1_assistant/config.py`
- [X] T005 [P] Implement shared model-source normalization and validation helpers in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T006 Wire shared startup validation errors into evaluation flow in `l1-assistant/src/l1_assistant/evaluation.py`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Choose Evaluation Model Source at Runtime (Priority: P1) 🎯 MVP

**Goal**: Let evaluators select local or Portkey-backed models per run through CLI parameters without editing project files.

**Independent Test**: Run `evaluate` once with `--model-source local` and once with `--model-source portkey`, then verify each run resolves the requested source and model identifiers.

### Tests for User Story 1

- [ ] T007 [P] [US1] Add CLI parsing coverage for model-source and per-role model options in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [ ] T008 [P] [US1] Add pipeline tests for local and Portkey model construction paths in `l1-assistant/tests/unit/test_pipeline.py`

### Implementation for User Story 1

- [X] T009 [US1] Add `evaluate` command options for model source and per-role model identifiers in `l1-assistant/src/l1_assistant/cli.py`
- [X] T010 [US1] Implement local and Portkey-backed model construction routing in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T011 [US1] Pass resolved runtime model configuration through evaluation execution in `l1-assistant/src/l1_assistant/evaluation.py`
- [ ] T012 [US1] Update evaluation command examples for local and Portkey-backed runs in `specs/006-model-selectable-evaluation/contracts/evaluation-model-cli.md`

**Checkpoint**: User Story 1 should support runtime source selection and be testable independently.

---

## Phase 4: User Story 2 - Validate Model Configuration Before Evaluation (Priority: P1)

**Goal**: Fail fast on incomplete, conflicting, or unavailable model-source configurations before question processing begins.

**Independent Test**: Run `evaluate` with missing Portkey settings, conflicting source inputs, and unavailable local models, then verify each case exits early with a clear corrective message.

### Tests for User Story 2

- [ ] T013 [P] [US2] Add validation failure coverage for conflicting and incomplete model-source inputs in `l1-assistant/tests/unit/test_evaluation.py`
- [ ] T014 [P] [US2] Add CLI integration coverage for pre-run validation failures in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 2

- [ ] T015 [US2] Enforce source-specific required inputs and conflict detection in `l1-assistant/src/l1_assistant/cli.py`
- [ ] T016 [US2] Implement startup availability checks for local and Portkey-backed runs in `l1-assistant/src/l1_assistant/pipeline.py`
- [ ] T017 [US2] Surface actionable validation and availability messages in `l1-assistant/src/l1_assistant/evaluation.py`
- [ ] T018 [US2] Document failure scenarios and expected operator behavior in `specs/006-model-selectable-evaluation/quickstart.md`

**Checkpoint**: User Story 2 should reject invalid configurations before evaluation starts and remain independently testable.

---

## Phase 5: User Story 3 - Preserve Comparable Reporting Across Model Sources (Priority: P2)

**Goal**: Persist model-source context in reports while preserving the existing evaluation result structure for comparison.

**Independent Test**: Generate one local report and one Portkey-backed report, then verify both contain explicit model-source context and unchanged metric/result structure.

### Tests for User Story 3

- [ ] T019 [P] [US3] Add report regression coverage for model-source metadata in `l1-assistant/tests/unit/test_evaluation.py`
- [ ] T020 [P] [US3] Add end-to-end report context assertions for local and Portkey-backed runs in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 3

- [ ] T021 [US3] Extend evaluation report configuration models for model-source context in `l1-assistant/src/l1_assistant/models.py`
- [ ] T022 [US3] Persist resolved model-source metadata in saved evaluation reports in `l1-assistant/src/l1_assistant/evaluation.py`
- [ ] T023 [US3] Emit user-facing runtime source and model summaries during evaluation startup in `l1-assistant/src/l1_assistant/cli.py`
- [ ] T024 [US3] Align report context documentation with implemented output in `specs/006-model-selectable-evaluation/data-model.md`

**Checkpoint**: User Story 3 should preserve comparable reports with explicit model-source provenance.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final consistency, validation, and cleanup across all stories.

- [ ] T025 [P] Add or update shared evaluation fixtures for Portkey-backed scenarios in `l1-assistant/tests/fixtures/`
- [ ] T026 Verify quickstart validation scenarios and update wording in `specs/006-model-selectable-evaluation/quickstart.md`
- [ ] T027 Run full evaluation regression coverage and capture any follow-up fixes in `l1-assistant/tests/unit/test_evaluation.py` and `l1-assistant/tests/integration/test_evaluation_cli.py`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on Foundational completion and reuses User Story 1 CLI/runtime selection paths
- **User Story 3 (Phase 5)**: Depends on Foundational completion and the resolved runtime configuration from User Stories 1 and 2
- **Polish (Phase 6)**: Depends on all user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Starts after Foundational completion; establishes runtime source selection MVP
- **User Story 2 (P1)**: Starts after Foundational completion; builds on the same runtime configuration path and should follow User Story 1 for the cleanest delivery
- **User Story 3 (P2)**: Starts after User Stories 1 and 2 because reporting depends on resolved and validated runtime configuration

### Within Each User Story

- Tests should be written before implementation and fail before code changes are completed
- CLI option parsing before runtime orchestration
- Runtime orchestration before report persistence
- Documentation updates after behavior is implemented and verified

### Parallel Opportunities

- `T004` and `T005` can run in parallel after `T003`
- `T007` and `T008` can run in parallel for User Story 1
- `T013` and `T014` can run in parallel for User Story 2
- `T019` and `T020` can run in parallel for User Story 3
- `T025` can run in parallel with documentation cleanup tasks in Phase 6

---

## Parallel Example: User Story 1

```bash
# Launch User Story 1 test work together:
Task: "Add CLI parsing coverage for model-source and per-role model options in l1-assistant/tests/integration/test_evaluation_cli.py"
Task: "Add pipeline tests for local and Portkey model construction paths in l1-assistant/tests/unit/test_pipeline.py"
```

## Parallel Example: User Story 2

```bash
# Launch User Story 2 validation tests together:
Task: "Add validation failure coverage for conflicting and incomplete model-source inputs in l1-assistant/tests/unit/test_evaluation.py"
Task: "Add CLI integration coverage for pre-run validation failures in l1-assistant/tests/integration/test_evaluation_cli.py"
```

## Parallel Example: User Story 3

```bash
# Launch User Story 3 report verification together:
Task: "Add report regression coverage for model-source metadata in l1-assistant/tests/unit/test_evaluation.py"
Task: "Add end-to-end report context assertions for local and Portkey-backed runs in l1-assistant/tests/integration/test_evaluation_cli.py"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate local and Portkey-backed runtime source selection through CLI tests
5. Demo the feature with one local run and one Portkey-backed run

### Incremental Delivery

1. Finish Setup + Foundational to establish shared runtime configuration
2. Deliver User Story 1 for runtime source selection
3. Deliver User Story 2 for fail-fast validation and availability checks
4. Deliver User Story 3 for report provenance and comparison support
5. Finish Polish phase and rerun quickstart validation

### Parallel Team Strategy

1. One developer completes Setup + Foundational tasks
2. After foundation is ready:
   - Developer A: User Story 1 runtime selection
   - Developer B: User Story 2 validation behavior
   - Developer C: User Story 3 reporting updates
3. Rejoin for Phase 6 regression validation and documentation cleanup

---

## Notes

- All tasks follow the required checklist format with task ID, optional parallel marker, story label where required, and exact file paths.
- User Story 1 is the suggested MVP scope.
- User Story 2 and User Story 3 remain independently testable once the shared foundation is complete.
- No extension hooks were executed because no `.specify/extensions.yml` hooks were present.
