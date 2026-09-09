---

description: "Task list for implementing Portkey chat model selection with chunking-aware evaluation"
---

# Tasks: Portkey Chat Chunking

**Input**: Design documents from `/specs/007-portkey-chat-chunking/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Include pytest unit and integration coverage because the plan and quickstart explicitly require validation through `tests/unit/test_pipeline.py`, `tests/unit/test_evaluation.py`, and `tests/integration/test_evaluation_cli.py`.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- Primary project: `l1-assistant/`
- Source code: `l1-assistant/src/l1_assistant/`
- Tests: `l1-assistant/tests/`
- Feature docs: `specs/007-portkey-chat-chunking/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Align dependencies and documentation scaffolding for Portkey-backed chat-model runs.

- [X] T001 Update Portkey and LangChain dependency declarations in `l1-assistant/pyproject.toml`
- [X] T002 [P] Align Portkey-backed chat-model usage examples in `l1-assistant/README.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Establish shared runtime configuration and validation primitives used by all evaluation stories.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T003 Extend runtime chat-model source and report context dataclasses in `l1-assistant/src/l1_assistant/models.py`
- [X] T004 [P] Add environment-backed Portkey chat-model defaults in `l1-assistant/src/l1_assistant/config.py`
- [X] T005 Implement startup resolution and fail-fast validation for chat-model source plus chunking settings in `l1-assistant/src/l1_assistant/pipeline.py`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Select Portkey Chat Evaluation Runs (Priority: P1) 🎯 MVP

**Goal**: Allow each evaluation run to choose a local or Portkey-backed chat model for answer generation without changing local defaults.

**Independent Test**: Run `l1-assistant evaluate` with `--model-source portkey`, a Portkey chat model, and required connection settings, then confirm startup uses the requested remote chat model while local-default runs still resolve to local behavior.

### Tests for User Story 1

- [X] T006 [P] [US1] Add runtime model-source resolution coverage in `l1-assistant/tests/unit/test_pipeline.py`
- [X] T007 [P] [US1] Add judge and chat-model construction coverage for local and Portkey-backed runs in `l1-assistant/tests/unit/test_evaluation.py`

### Implementation for User Story 1

- [X] T008 [US1] Implement Portkey-backed and local chat/judge model construction in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T009 [US1] Wire `evaluate` CLI model-source, chat-model, judge-model, and Portkey options in `l1-assistant/src/l1_assistant/cli.py`
- [X] T010 [US1] Integrate resolved runtime model configuration into evaluation execution in `l1-assistant/src/l1_assistant/pipeline.py`

**Checkpoint**: User Story 1 should support local-default and Portkey-backed chat-model evaluation runs.

---

## Phase 4: User Story 2 - Combine Remote Chat Models with Chunking Choices (Priority: P1)

**Goal**: Preserve all supported chunking strategies when the chat model is Portkey-backed and validate strategy-specific settings before evaluation starts.

**Independent Test**: Run `l1-assistant evaluate` with a Portkey-backed chat model and each supported chunking strategy, confirming the selected strategy and settings are applied without changing the chosen chat-model source.

### Tests for User Story 2

- [X] T011 [P] [US2] Add chunking-validation coverage for Portkey-backed runs in `l1-assistant/tests/unit/test_pipeline.py`
- [X] T012 [P] [US2] Add CLI integration coverage for Portkey-backed recursive and semantic chunking runs in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 2

- [X] T013 [US2] Preserve chunking-strategy-specific configuration alongside chat-model source resolution in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T014 [US2] Update evaluation command startup summaries for chunking plus chat-model source in `l1-assistant/src/l1_assistant/cli.py`

**Checkpoint**: Portkey-backed chat-model runs should work with all supported chunking strategies and fail fast on invalid chunking inputs.

---

## Phase 5: User Story 3 - Preserve Clear Run Context for Comparison (Priority: P2)

**Goal**: Persist comparable report metadata that clearly identifies chat-model source, model identifiers, and chunking context for local and Portkey-backed runs.

**Independent Test**: Generate local and Portkey-backed evaluation reports with different chunking strategies and confirm the saved configuration metadata identifies the resolved chat-model source and chunking strategy while preserving the existing result structure.

### Tests for User Story 3

- [X] T015 [P] [US3] Add report-context regression coverage in `l1-assistant/tests/unit/test_pipeline.py`
- [X] T016 [P] [US3] Add saved-report metadata assertions in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 3

- [X] T017 [US3] Persist chat-model source, model identifiers, chunking settings, and non-secret Portkey context in `l1-assistant/src/l1_assistant/models.py`
- [X] T018 [US3] Propagate report configuration metadata through evaluation report generation in `l1-assistant/src/l1_assistant/pipeline.py`

**Checkpoint**: Completed reports should remain structurally comparable while exposing clear run provenance.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final consistency, documentation, and end-to-end validation across all stories.

- [X] T019 [P] Reconcile Portkey and chunking examples with final CLI behavior in `l1-assistant/README.md`
- [ ] T020 Run quickstart validation scenarios from `specs/007-portkey-chat-chunking/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on Foundational completion and reuses User Story 1 runtime model wiring
- **User Story 3 (Phase 5)**: Depends on Foundational completion and the runtime/report plumbing established in User Stories 1 and 2
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Starts after Phase 2 and delivers the MVP remote chat-model selection path
- **User Story 2 (P1)**: Starts after Phase 2 but should land after User Story 1 because it extends the same evaluation wiring with chunking-specific validation
- **User Story 3 (P2)**: Starts after User Stories 1 and 2 because it depends on final runtime and chunking context being available for report persistence

### Within Each User Story

- Tests should be added before or alongside implementation and must fail before the corresponding implementation is considered complete
- Runtime configuration models and validation precede CLI and orchestration wiring
- CLI startup output changes follow resolved runtime configuration changes
- Report metadata changes follow runtime and chunking integration changes

### Parallel Opportunities

- `T002` can run in parallel with `T001`
- `T004` can run in parallel with `T003`
- `T006` and `T007` can run in parallel within User Story 1
- `T011` and `T012` can run in parallel within User Story 2
- `T015` and `T016` can run in parallel within User Story 3
- `T019` can run in parallel with final validation preparation before `T020`

---

## Parallel Example: User Story 1

```bash
# Launch User Story 1 test work together:
Task: "Add runtime model-source resolution coverage in l1-assistant/tests/unit/test_pipeline.py"
Task: "Add judge and chat-model construction coverage for local and Portkey-backed runs in l1-assistant/tests/unit/test_evaluation.py"
```

## Parallel Example: User Story 2

```bash
# Launch User Story 2 validation coverage together:
Task: "Add chunking-validation coverage for Portkey-backed runs in l1-assistant/tests/unit/test_pipeline.py"
Task: "Add CLI integration coverage for Portkey-backed recursive and semantic chunking runs in l1-assistant/tests/integration/test_evaluation_cli.py"
```

## Parallel Example: User Story 3

```bash
# Launch User Story 3 report verification together:
Task: "Add report-context regression coverage in l1-assistant/tests/unit/test_pipeline.py"
Task: "Add saved-report metadata assertions in l1-assistant/tests/integration/test_evaluation_cli.py"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Confirm local-default and Portkey-backed chat-model runs both resolve correctly
5. Demo the remote chat-model selection workflow

### Incremental Delivery

1. Complete Setup + Foundational
2. Deliver User Story 1 for remote chat-model selection
3. Deliver User Story 2 for chunking compatibility across local and Portkey-backed runs
4. Deliver User Story 3 for report provenance and comparison support
5. Finish with documentation reconciliation and quickstart validation

### Parallel Team Strategy

With multiple developers:

1. One developer updates shared runtime models/configuration in Phase 2
2. One developer focuses on CLI and evaluation wiring for User Story 1
3. One developer adds chunking validation and CLI integration coverage for User Story 2 after Phase 2 stabilizes
4. One developer finalizes report metadata and regression coverage for User Story 3

---

## Notes

- [P] tasks touch different files or can proceed without waiting on incomplete same-file edits
- [US1], [US2], and [US3] labels map directly to the stories in `specs/007-portkey-chat-chunking/spec.md`
- Each user story remains independently testable using the criteria defined in the corresponding phase
- The MVP scope is User Story 1 because it delivers the requested Portkey-backed chat-model selection path