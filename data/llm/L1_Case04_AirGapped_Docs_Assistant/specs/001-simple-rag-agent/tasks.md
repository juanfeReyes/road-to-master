---

description: "Implementation tasks for the Simple RAG Agent"
---

# Tasks: Simple RAG Agent

**Input**: Design documents from `/specs/001-simple-rag-agent/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/cli.md, quickstart.md

**Organization**: Tasks are grouped by user story so each story can be implemented and tested independently.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Configure the existing Python package and dependency entry points.

- [X] T001 Update `l1-assistant/pyproject.toml` with LangChain, local HuggingFace embeddings, Chroma, Ollama adapter, and pytest dependencies
- [X] T002 [P] Create the planned package modules under `l1-assistant/src/l1_assistant/` with public imports in `__init__.py`
- [X] T003 [P] Add generated vector-store and model-cache paths to `l1-assistant/.gitignore`
- [X] T004 [P] Create test package directories `l1-assistant/tests/unit/` and `l1-assistant/tests/integration/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Establish shared configuration, domain types, and explicit offline/error behavior before story work.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [X] T005 Implement typed runtime configuration and path validation in `l1-assistant/src/l1_assistant/config.py` for data directory, Chroma directory, local embedding model, local chat model, and retrieval settings
- [X] T006 [P] Define `SourceDocument`, `DocumentPassage`, `UserQuestion`, `GroundedResponse`, and `IndexSnapshot` types in `l1-assistant/src/l1_assistant/models.py`
- [X] T007 [P] Add structured application errors and stderr-safe logging helpers in `l1-assistant/src/l1_assistant/errors.py`
- [X] T008 [P] Add deterministic test fixtures and local fake embeddings/chat model in `l1-assistant/tests/conftest.py`
- [X] T009 Document offline provisioning and environment variables in `l1-assistant/README.md`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel.

---

## Phase 3: User Story 1 - Ask Questions About Local Documentation (Priority: P1) 🎯 MVP

**Goal**: Build and query a local LangChain RAG pipeline that answers from Markdown evidence and declines unsupported questions.

**Independent Test**: Index the supplied Markdown corpus, ask a question with a known answer, and verify the answer, source filename, and offline behavior; ask an unsupported question and verify an insufficient-evidence response.

### Tests for User Story 1

- [X] T010 [P] [US1] Add unit tests for question validation and insufficient-evidence behavior in `l1-assistant/tests/unit/test_answering.py`
- [X] T011 [P] [US1] Add integration tests for grounded answers, source reporting, and no-network operation in `l1-assistant/tests/integration/test_rag_flow.py`
- [X] T012 [P] [US1] Add CLI contract tests for `ask` output and non-zero failures in `l1-assistant/tests/integration/test_cli_ask.py`

### Implementation for User Story 1

- [X] T013 [P] [US1] Implement Markdown loading, section-aware passage creation, and source metadata preservation in `l1-assistant/src/l1_assistant/ingestion.py`
- [X] T014 [P] [US1] Implement Chroma persistence and similarity retrieval over local embeddings in `l1-assistant/src/l1_assistant/retrieval.py`
- [X] T015 [US1] Implement grounded prompt construction, local Ollama-compatible generation, and insufficient-evidence handling in `l1-assistant/src/l1_assistant/answering.py` (depends on T013, T014)
- [X] T016 [US1] Implement `ask` command parsing, answer/source formatting, and actionable error exits in `l1-assistant/src/l1_assistant/cli.py` (depends on T015)
- [X] T017 [US1] Wire the console entry point to the CLI in `l1-assistant/src/l1_assistant/__init__.py` (depends on T016)

**Checkpoint**: User Story 1 is independently functional and provides the MVP.

---

## Phase 4: User Story 2 - Keep the Document Index Current (Priority: P2)

**Goal**: Rebuild the local vector index from the current Markdown corpus, including additions, changes, removals, and recoverable file errors.

**Independent Test**: Build an index, modify/add/remove files, rebuild it, and verify that retrieval reflects only the current readable corpus.

### Tests for User Story 2

- [X] T018 [P] [US2] Add ingestion unit tests for discovery, chunk metadata, malformed-file reporting, and both chunk strategies in `l1-assistant/tests/unit/test_ingestion.py`
- [X] T019 [P] [US2] Add integration tests for Chroma rebuilds removing stale passages in `l1-assistant/tests/integration/test_index_rebuild.py`
- [X] T020 [P] [US2] Add CLI contract tests for `index` counts and failures in `l1-assistant/tests/integration/test_cli_index.py`

### Implementation for User Story 2

- [X] T021 [US2] Extend `l1-assistant/src/l1_assistant/ingestion.py` with fixed-size chunking, deterministic passage IDs, content hashes, and current-corpus reconciliation (depends on T013)
- [X] T022 [US2] Implement index build/load lifecycle and stale-vector removal in `l1-assistant/src/l1_assistant/retrieval.py` (depends on T014, T021)
- [X] T023 [US2] Implement `index` CLI command with data validation, processing warnings, counts, and chunk-strategy options in `l1-assistant/src/l1_assistant/cli.py` (depends on T022)
- [X] T024 [US2] Update `l1-assistant/src/l1_assistant/__init__.py` entry-point dispatch for `index` and `ask` (depends on T023)

**Checkpoint**: User Stories 1 and 2 both work independently; the index can be rebuilt without source changes.

---

## Phase 5: User Story 3 - Inspect Response Quality (Priority: P3)

**Goal**: Calculate and print a transparent 0-1 evidence-support score for every completed response.

**Independent Test**: Compare a strongly supported response with a weak/unsupported response and verify the numeric scale, ordering, and unavailable-score behavior.

### Tests for User Story 3

- [X] T025 [P] [US3] Add scoring unit tests for relevance, citation presence, answer/evidence overlap, bounds, and unavailable scoring in `l1-assistant/tests/unit/test_scoring.py`
- [X] T026 [P] [US3] Add integration tests for score output on grounded and insufficient-evidence answers in `l1-assistant/tests/integration/test_score_output.py`

### Implementation for User Story 3

- [X] T027 [US3] Implement deterministic evidence-support scoring and score explanations on the documented 0-1 scale in `l1-assistant/src/l1_assistant/scoring.py`
- [X] T028 [US3] Attach scoring to the `GroundedResponse` flow and print `Score: N.NN/1.00` or `Score: unavailable` in `l1-assistant/src/l1_assistant/answering.py` and `l1-assistant/src/l1_assistant/cli.py` (depends on T027)
- [X] T029 [US3] Record per-question score and source results for evaluation runs in `l1-assistant/src/l1_assistant/evaluation.py` (depends on T027)

**Checkpoint**: All user stories are independently functional and every completed response exposes a score or explicit unavailability.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Complete required measurements, documentation, and end-to-end validation.

- [X] T030 [P] Implement the two-strategy retrieval comparison over `data/engineer_questions.csv` in `l1-assistant/experiments/retrieval_comparison.py`
- [X] T031 [P] Add optional, isolated gateway comparison configuration and measured latency/cost/quality reporting in `l1-assistant/experiments/gateway_comparison.py`
- [X] T032 [P] Document measured chunking results, wrong question IDs, and the local-versus-gateway quality tradeoff in `l1-assistant/EXPERIMENT.md`
- [X] T033 Update `l1-assistant/README.md` with index/ask examples, score semantics, source citation behavior, and error handling
- [X] T034 Run the complete pytest suite and the `quickstart.md` validation scenarios from `l1-assistant/`
- [X] T035 Review generated data paths, offline guarantees, and dependency lockfile updates in `l1-assistant/uv.lock`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies.
- **Foundational (Phase 2)**: Depends on Setup and blocks all user stories.
- **User Stories (Phases 3-5)**: Depend on Foundational completion.
- **Polish (Phase 6)**: Depends on the desired user stories being complete.

### User Story Dependencies

- **US1 (P1)**: Starts after Phase 2; no dependency on other stories; recommended MVP.
- **US2 (P2)**: Starts after Phase 2; extends ingestion/retrieval but must preserve US1 behavior.
- **US3 (P3)**: Starts after Phase 2; integrates with answer output and can be tested with deterministic responses.

### Parallel Opportunities

- T002-T004 and T006-T008 can run in parallel.
- T010-T012, T013-T014 can run in parallel within US1.
- T018-T020 and T021 can run in parallel within US2 after foundational setup.
- T025-T026 and T027 can run in parallel within US3.
- T030-T033 can run in parallel after story implementation.

## Implementation Strategy

1. Complete setup and foundational configuration.
2. Deliver US1 as the smallest usable offline RAG MVP.
3. Add reliable rebuild behavior in US2.
4. Add transparent scoring and evaluation records in US3.
5. Run the required chunking and gateway measurements, then validate the quickstart end to end.

