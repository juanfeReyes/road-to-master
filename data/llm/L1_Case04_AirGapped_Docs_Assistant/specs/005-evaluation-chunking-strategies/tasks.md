---

description: "Implementation tasks for evaluation chunking strategies"
---

# Tasks: Evaluation Chunking Strategies

**Input**: Design documents from `specs/005-evaluation-chunking-strategies/`

**Prerequisites**: `plan.md`, `spec.md`, `research.md`, `data-model.md`, `contracts/chunking-cli.md`, `quickstart.md`

**Tests**: Included because the feature specification defines independent test criteria and measurable strategy/report outcomes.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Prepare dependencies, fixtures, and test seams for strategy-aware ingestion.

- [X] T001 [P] Add `langchain-experimental` to `l1-assistant/pyproject.toml` and refresh `l1-assistant/uv.lock` for offline semantic chunking support
- [X] T002 [P] Add representative Markdown documents with headings, paragraphs, delimiters, and semantically distinct sections in `l1-assistant/tests/fixtures/chunking/`
- [X] T003 [P] Add shared local embedding/splitter test doubles and source-document fixtures in `l1-assistant/tests/conftest.py`
- [X] T004 [P] Add a strategy comparison evaluation fixture and expected provenance assertions in `l1-assistant/tests/fixtures/chunking/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Establish the normalized chunking configuration and shared provenance contract before implementing individual strategies.

- [X] T005 [P] Add `ChunkingConfig` and chunking strategy types with serializable effective defaults in `l1-assistant/src/l1_assistant/models.py`
- [X] T006 [P] Implement strategy-specific argument validation for sizes, overlap, separators, semantic threshold type/amount, and embedding prerequisites in `l1-assistant/src/l1_assistant/chunking.py`
- [X] T007 [P] Implement a LangChain splitter factory boundary for fixed, recursive, and semantic strategies in `l1-assistant/src/l1_assistant/chunking.py`
- [X] T008 [P] Add unit tests for configuration defaults, serialization, validation failures, and splitter selection in `l1-assistant/tests/unit/test_chunking.py`
- [X] T009 [P] Extend `DocumentPassage` or its metadata contract to carry effective chunking strategy and source provenance in `l1-assistant/src/l1_assistant/models.py`

**Checkpoint**: Validated configuration and splitter factory are ready; strategy-specific ingestion can begin.

---

## Phase 3: User Story 1 - Select Chunking for Evaluation (Priority: P1) 🎯 MVP

**Goal**: Let evaluators run fixed-size, recursive, or semantic chunking through one command option and use the selected passages in retrieval and DeepEval evaluation.

**Independent Test**: Build the local retriever for each supported strategy against the same fixture corpus and verify strategy-specific passages reach evaluation in stable order.

### Tests for User Story 1

- [X] T010 [P] [US1] Test fixed-size splitting creates bounded overlapping passages and stable passage IDs in `l1-assistant/tests/unit/test_chunking.py`
- [X] T011 [P] [US1] Test recursive splitting prefers configured paragraph/newline/word separators before character fallback in `l1-assistant/tests/unit/test_chunking.py`
- [X] T012 [P] [US1] Test semantic splitting uses local embeddings and fails explicitly when semantic dependencies are unavailable in `l1-assistant/tests/unit/test_chunking.py`
- [X] T013 [P] [US1] Test `LocalRetriever.build` applies each strategy and preserves source/heading/order metadata in `l1-assistant/tests/unit/test_retrieval.py`
- [X] T014 [P] [US1] Test evaluate CLI accepts `--chunking-strategy` and strategy-specific options in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 1

- [X] T015 [US1] Implement fixed-size LangChain character splitting with configured size and overlap in `l1-assistant/src/l1_assistant/chunking.py`
- [X] T016 [US1] Implement recursive LangChain splitting with explicit ordered separators in `l1-assistant/src/l1_assistant/chunking.py`
- [X] T017 [US1] Implement semantic LangChain splitting with local embeddings, breakpoint controls, and explicit prerequisite errors in `l1-assistant/src/l1_assistant/chunking.py`
- [X] T018 [US1] Replace or route existing ingestion chunking through the strategy splitter factory while preserving document discovery and heading metadata in `l1-assistant/src/l1_assistant/ingestion.py`
- [X] T019 [US1] Pass normalized chunking configuration into `LocalRetriever.build` and preserve it when creating retrieval passages in `l1-assistant/src/l1_assistant/retrieval.py`
- [X] T020 [US1] Add `--chunking-strategy`, size, overlap, separator, embedding-model, and semantic breakpoint options to `index` and `evaluate` in `l1-assistant/src/l1_assistant/cli.py`
- [X] T021 [US1] Wire CLI chunking configuration into evaluation retriever construction without changing dataset, metric, judge, tracing, or question-limit behavior in `l1-assistant/src/l1_assistant/cli.py`

**Checkpoint**: All three strategies can be explicitly selected and used by the evaluation workflow.

---

## Phase 4: User Story 2 - Preserve Evaluation Comparability (Priority: P1)

**Goal**: Keep evaluation inputs, metrics, thresholds, ordering, and report structure comparable while recording the effective chunking configuration.

**Independent Test**: Run equivalent evaluations with different strategies and compare report configuration, question order, metric definitions, and aggregate/result shapes.

### Tests for User Story 2

- [X] T022 [P] [US2] Test evaluation reports contain `chunking_strategy`, serialized effective chunking settings, and embedding model provenance in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T023 [P] [US2] Test strategy comparisons preserve dataset identity, metric selection, thresholds, question order, and result fields in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T024 [P] [US2] Test repeated builds with identical source/configuration produce equivalent passage IDs and provenance in `l1-assistant/tests/unit/test_retrieval.py`
- [X] T025 [P] [US2] Test a changed strategy or effective setting changes index identity and does not silently reuse incompatible persisted chunks in `l1-assistant/tests/unit/test_retrieval.py`

### Implementation for User Story 2

- [X] T026 [US2] Add serialized chunking configuration and effective strategy fields to evaluation report configuration in `l1-assistant/src/l1_assistant/cli.py`
- [X] T027 [US2] Include source hashes, embedding model, and chunking configuration in retriever/index identity metadata in `l1-assistant/src/l1_assistant/retrieval.py`
- [X] T028 [US2] Ensure passage IDs and source/heading/order metadata remain stable and strategy-aware across fixed and recursive builds in `l1-assistant/src/l1_assistant/ingestion.py`
- [X] T029 [US2] Preserve semantic provenance without claiming unreliable exact offsets and expose actionable metadata for retrieved passages in `l1-assistant/src/l1_assistant/retrieval.py`
- [X] T030 [US2] Preserve omitted-option compatibility by mapping the existing default behavior to a documented effective chunking configuration in `l1-assistant/src/l1_assistant/cli.py` and `l1-assistant/src/l1_assistant/chunking.py`

**Checkpoint**: Reports and persisted retrieval state are reproducible and comparable across strategy runs.

---

## Phase 5: User Story 3 - Validate Strategy Selection (Priority: P2)

**Goal**: Reject unsupported strategies, invalid settings, missing semantic resources, and indexing failures without silently falling back or claiming successful metrics.

**Independent Test**: Invoke CLI and splitter construction with invalid values and missing prerequisites, then verify clear non-zero errors and no successful report claim.

### Tests for User Story 3

- [X] T031 [P] [US3] Test unsupported strategy values are rejected with supported choices by the CLI parser in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T032 [P] [US3] Test invalid size, overlap, separator, and semantic threshold combinations fail before indexing in `l1-assistant/tests/unit/test_chunking.py`
- [X] T033 [P] [US3] Test missing semantic package/model/embedding resources produce actionable errors with no fallback in `l1-assistant/tests/unit/test_chunking.py`
- [X] T034 [P] [US3] Test index-build failures propagate through `evaluate` without writing a successful report in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 3

- [X] T035 [US3] Add argparse choices and cross-option validation for all chunking CLI parameters in `l1-assistant/src/l1_assistant/cli.py`
- [X] T036 [US3] Surface missing `langchain-experimental`, NumPy, embedding model, and local model-file errors with strategy-specific remediation in `l1-assistant/src/l1_assistant/chunking.py`
- [X] T037 [US3] Prevent automatic strategy fallback and ensure failed chunking/index construction aborts before metric evaluation in `l1-assistant/src/l1_assistant/retrieval.py` and `l1-assistant/src/l1_assistant/cli.py`
- [X] T038 [US3] Keep existing `index`, `ask`, `pipeline`, and no-option `evaluate` invocations compatible while applying the documented default strategy in `l1-assistant/src/l1_assistant/cli.py`

**Checkpoint**: Invalid or unavailable strategies fail safely and existing workflows remain compatible.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Document, validate, and regression-test the complete strategy-aware evaluation workflow.

- [X] T039 [P] Update `l1-assistant/README.md` with fixed, recursive, and semantic commands, defaults, prerequisites, and report fields
- [X] T040 [P] Update `specs/005-evaluation-chunking-strategies/quickstart.md` and `specs/005-evaluation-chunking-strategies/contracts/chunking-cli.md` if final option names or defaults change
- [X] T041 [P] Add report-schema assertions for chunking provenance while preserving existing JSONL/CSV evaluation fields in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T042 [P] Add a representative strategy comparison fixture/test covering at least 100 questions or equivalent row-order coverage in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T043 Run targeted chunking, retrieval, CLI, and existing evaluation tests with the project Python environment
- [X] T044 Run the complete pytest suite and Python compilation checks
- [X] T045 Run all validation scenarios from `specs/005-evaluation-chunking-strategies/quickstart.md` and verify report provenance manually

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No implementation dependency; dependency and fixture tasks can start immediately.
- **Foundational (Phase 2)**: Depends on Setup and blocks all user stories.
- **User Story 1 (Phase 3)**: Depends on the validated config/splitter boundary and delivers the MVP.
- **User Story 2 (Phase 4)**: Depends on US1 strategy-aware passages and retriever wiring.
- **User Story 3 (Phase 5)**: Depends on US1 CLI options and US2 provenance/index identity.
- **Polish (Phase 6)**: Depends on all required stories.

### User Story Dependencies

- **US1 (P1)**: Can start after Phase 2; no dependency on another story.
- **US2 (P1)**: Depends on US1's strategy-aware ingestion and retrieval path.
- **US3 (P2)**: Depends on US1's CLI/configuration path and US2's persisted identity/report behavior.

### Parallel Opportunities

- Phase 1 tasks T001-T004 can run in parallel.
- Foundational tasks T005-T009 can run in parallel once the config shape is agreed.
- US1 tests T010-T014 can be authored in parallel; fixed, recursive, and semantic splitter tasks T015-T017 can proceed in parallel after T007.
- US2 tests T022-T025 can run in parallel; report and identity implementation can proceed in separate files.
- US3 validation tests T031-T034 can run in parallel; CLI and splitter error handling can proceed independently.
- Documentation and report assertions T039-T042 can run in parallel during final validation.

## Parallel Example: User Story 1

```text
Task: "Implement fixed-size splitter and unit tests in l1-assistant/src/l1_assistant/chunking.py and l1-assistant/tests/unit/test_chunking.py"
Task: "Implement recursive splitter and separator tests in l1-assistant/src/l1_assistant/chunking.py and l1-assistant/tests/unit/test_chunking.py"
Task: "Add CLI strategy parser coverage in l1-assistant/tests/integration/test_evaluation_cli.py"
```

## Parallel Example: User Story 2

```text
Task: "Add evaluation report provenance assertions in l1-assistant/tests/integration/test_evaluation_cli.py"
Task: "Add index identity regression tests in l1-assistant/tests/unit/test_retrieval.py"
Task: "Implement report configuration serialization in l1-assistant/src/l1_assistant/cli.py"
```

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1 and Phase 2.
2. Implement fixed and recursive splitters, semantic splitter prerequisites,
   strategy-aware ingestion, retriever wiring, and CLI selection.
3. Run US1 targeted tests and validate one evaluation per strategy.

### Incremental Delivery

1. Add US2 report/index provenance and comparability safeguards.
2. Add US3 validation and no-fallback behavior.
3. Complete documentation, representative comparison coverage, full regression
   tests, and quickstart validation.

### Format Validation

All 45 tasks use the required checklist format: checkbox, sequential task ID,
optional `[P]`, required story label in story phases, and an explicit file path.
