---

description: "Task list for implementing the DeepEval evaluation pipeline"
---

# Tasks: Agent Evaluation Pipeline

**Input**: Design documents from `specs/003-agent-evaluation-pipeline/`

**Prerequisites**: [plan.md](./plan.md), [spec.md](./spec.md), [research.md](./research.md), [data-model.md](./data-model.md), [contracts/evaluation-cli.md](./contracts/evaluation-cli.md), and [quickstart.md](./quickstart.md)

**Tests**: Included because the plan and quickstart explicitly require focused unit and integration coverage.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Add the dependency and fixture/configuration foundations required by every user story.

- [X] T001 Pin the approved DeepEval release and add any required local-evaluator support dependency in `l1-assistant/pyproject.toml`
- [X] T002 [P] Add a representative JSONL evaluation fixture with supported, unsupported, missing-reference, multi-source, and duplicate-input cases in `l1-assistant/tests/fixtures/evaluation.jsonl`
- [X] T003 [P] Add shared test factories for evaluation datasets, grounded responses, passages, fake judges, and fake retrievers in `l1-assistant/tests/conftest.py`
- [X] T004 [P] Document local judge model configuration and DeepEval network/telemetry-off settings in `l1-assistant/README.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Build the shared models, validation, local judge boundary, and retrieval capture needed before either metric story can be implemented.

**âš ï¸ CRITICAL**: No user story work can begin until this phase is complete.

- [X] T005 Define typed evaluation configuration, dataset record, metric definition/result, retrieved passage, question evaluation, and report models in `l1-assistant/src/l1_assistant/models.py`
- [X] T006 Implement streaming JSONL dataset loading, dataset hashing, unique-ID validation, reference-field validation, and `max_questions` handling in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T007 [P] Extend `l1-assistant/src/l1_assistant/retrieval.py` to expose ordered passages with source IDs, passage IDs, ranks, and optional retrieval scores without changing existing search behavior
- [X] T008 [P] Extend `l1-assistant/src/l1_assistant/answering.py` with an evaluation-safe response path that returns answer text and ordered retrieval context while preserving the existing `ask` behavior
- [X] T009 Implement the local `DeepEvalBaseLLM` adapter with synchronous/asynchronous generation, model identity, structured-output handling, and explicit local-resource errors in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T010 Implement metric-definition factories, per-metric threshold configuration, eligibility checks, and deterministic blank-answer/source-ID checks in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T011 [P] Add local network/telemetry configuration guards and optional DeepEval/LangChain span helpers in `l1-assistant/src/l1_assistant/tracing.py`
- [X] T012 Add foundational tests for JSONL validation, dataset hashing, metric eligibility, local judge failures, and ordered retrieval serialization in `l1-assistant/tests/unit/test_evaluation.py`

**Checkpoint**: Shared models, dataset validation, response capture, local judge adapter, and metric configuration are ready; user stories can proceed independently.

---

## Phase 3: User Story 1 - Evaluate Agent Responses (Priority: P1) ðŸŽ¯ MVP

**Goal**: Run each valid question through the existing local RAG assistant and calculate generator relevance, faithfulness, and reference-based correctness with explicit unavailable states.

**Independent Test**: Run the generator-only evaluation against `l1-assistant/tests/fixtures/evaluation.jsonl` with a fake local judge and verify one ordered result per valid question plus generator aggregates and reasons.

### Tests for User Story 1

- [X] T013 [P] [US1] Add unit tests for `LLMTestCase` construction with and without `expected_output` in `l1-assistant/tests/unit/test_evaluation.py`
- [X] T014 [P] [US1] Add unit tests verifying `AnswerRelevancyMetric`, `FaithfulnessMetric`, and `GEval` correctness results are mapped to `MetricResult` with scores, thresholds, pass state, and reasons in `l1-assistant/tests/unit/test_evaluation.py`
- [X] T015 [P] [US1] Add unit tests proving missing references mark correctness as unavailable while reference-independent generator metrics remain eligible in `l1-assistant/tests/unit/test_evaluation.py`
- [X] T016 [P] [US1] Add integration coverage for supported, unsupported, empty, and row-failure answers using fake local models in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 1

- [X] T017 [US1] Implement generator `LLMTestCase` construction from captured assistant responses and evaluation dataset records in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T018 [US1] Implement generator metric execution with bounded/synchronous local judge behavior and explicit exception-to-row-error mapping in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T019 [US1] Implement generator metric aggregation over eligible cases with per-metric denominators, score distributions, and unavailable reasons in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T020 [US1] Add generator-only orchestration that evaluates each valid dataset row independently and preserves input order in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T021 [US1] Verify the generator-only path satisfies FR-002, FR-004, FR-006, FR-007, FR-008, FR-015, and FR-016 using the focused unit and integration tests in `l1-assistant/tests/unit/test_evaluation.py` and `l1-assistant/tests/integration/test_evaluation_cli.py`

**Checkpoint**: User Story 1 is independently runnable and reports generator metrics without requiring retrieval reference fields.

---

## Phase 4: User Story 2 - Evaluate Retrieval Quality (Priority: P1)

**Goal**: Measure retrieved-context relevance, rank quality, and answer-support coverage while preserving source and passage order.

**Independent Test**: Run the retrieval-only evaluation against fixture questions with known `expected_sources` and `expected_output`, then verify hit/source checks and DeepEval contextual metric aggregates.

### Tests for User Story 2

- [X] T022 [P] [US2] Add unit tests for ordered `retrieval_context` serialization, one-based rank preservation, source IDs, passage IDs, and optional retrieval scores in `l1-assistant/tests/unit/test_evaluation.py`
- [X] T023 [P] [US2] Add unit tests mapping contextual relevancy, precision, and recall outputs to retrieval metric results and denominators in `l1-assistant/tests/unit/test_evaluation.py`
- [X] T024 [P] [US2] Add tests for no passages, irrelevant passages, multiple valid source IDs, and missing reference answers in `l1-assistant/tests/unit/test_evaluation.py`
- [X] T025 [P] [US2] Add integration coverage proving retrieval failures do not become generator passes and that later rows survive a retrieval exception in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 2

- [X] T026 [US2] Build DeepEval retrieval test cases from ordered captured passages and dataset references in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T027 [US2] Implement contextual relevancy, precision, and recall metric execution with reference-aware eligibility and local judge configuration in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T028 [US2] Implement deterministic expected-source hit/rank checks that accept any configured valid source and distinguish no-context from score zero in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T029 [US2] Implement retrieval aggregate calculations with eligible denominators and rank/source failure reasons in `l1-assistant/src/l1_assistant/evaluation.py`
- [X] T030 [US2] Integrate retrieval evaluation into the per-question pipeline result without changing existing `LocalRetriever.search` behavior in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T031 [US2] Verify the retrieval-only path satisfies FR-003, FR-005, FR-006, FR-007, FR-008, FR-015, and the retrieval acceptance scenarios using `l1-assistant/tests/unit/test_evaluation.py` and `l1-assistant/tests/integration/test_evaluation_cli.py`

**Checkpoint**: User Story 2 independently reports retrieval quality and preserves the evidence needed to diagnose ranking failures.

---

## Phase 5: User Story 3 - Review and Compare Evaluation Reports (Priority: P2)

**Goal**: Expose the evaluation command, local tracing option, stable report schema, aggregate summaries, and partial-failure exit behavior.

**Independent Test**: Run the full CLI with both metric groups and tracing disabled/enabled, inspect console output and JSON report, and compare two reports generated from the same dataset/configuration.

### Tests for User Story 3

- [X] T032 [P] [US3] Add CLI parser tests for `evaluate`, metric groups, dataset/output paths, judge model, question limit, threshold overrides, and `--trace` in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T033 [P] [US3] Add report serialization tests for schema version, dataset hash, model/configuration context, metric definitions, aggregates, counts, ordered results, and trace metadata in `l1-assistant/tests/unit/test_pipeline.py`
- [X] T034 [P] [US3] Add tests proving partial failures write successful rows, record actionable errors, print counts, and return the contract-defined non-zero status in `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T035 [P] [US3] Add offline-boundary tests asserting the local judge path does not invoke hosted evaluation or telemetry services in `l1-assistant/tests/integration/test_evaluation_cli.py`

### Implementation for User Story 3

- [X] T036 [US3] Implement report aggregation, schema-versioned JSON serialization, deterministic output handling, and run timing metadata in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T037 [US3] Implement optional local tracing around root evaluation, retrieval, answer generation, and judge phases in `l1-assistant/src/l1_assistant/tracing.py`
- [X] T038 [US3] Add the `evaluate` subcommand and contract options to `l1-assistant/src/l1_assistant/cli.py` while preserving `index`, `ask`, and `pipeline` commands
- [X] T039 [US3] Implement concise console rendering of dataset identity, metric aggregates, denominators, counts, failures, and report path in `l1-assistant/src/l1_assistant/pipeline.py`
- [X] T040 [US3] Implement non-zero CLI outcomes for invalid input, unavailable local resources, and partial evaluation according to `specs/003-agent-evaluation-pipeline/contracts/evaluation-cli.md` in `l1-assistant/src/l1_assistant/cli.py`
- [X] T041 [US3] Validate the full quickstart scenarios, including repeated-report schema comparison and network-denied execution, using `specs/003-agent-evaluation-pipeline/quickstart.md`

**Checkpoint**: All user stories are independently usable; the full evaluation command produces a durable, comparable report and optional local traces.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Harden the feature, calibrate thresholds, and verify compatibility with existing behavior.

- [X] T042 [P] Add a labeled calibration dataset covering supported, unsupported, ambiguous, unanswerable, multi-hop, and adversarial questions in `l1-assistant/tests/fixtures/evaluation-calibration.jsonl`
- [X] T043 [P] Document metric definitions, threshold calibration, local judge provisioning, offline guarantees, and report interpretation in `l1-assistant/README.md`
- [X] T044 [P] Add regression tests proving existing `ask`, `index`, and CSV `pipeline` behavior remains compatible in `l1-assistant/tests/unit/test_pipeline.py` and `l1-assistant/tests/integration/test_evaluation_cli.py`
- [X] T045 Run the focused evaluation suite and the complete existing pytest suite from `l1-assistant` and resolve failures without changing unrelated behavior
- [X] T046 Review report and trace payloads for accidental hosted endpoints or sensitive-data export and add safeguards in `l1-assistant/src/l1_assistant/tracing.py`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies; T001 is required before installing/running DeepEval tests, while T002-T004 can proceed in parallel.
- **Foundational (Phase 2)**: Depends on T001; T005-T011 can proceed in parallel where they touch different modules, and T012 depends on the foundational implementations.
- **User Stories (Phases 3-5)**: Depend on Phase 2 completion. US1 and US2 can proceed in parallel after the foundation because their metric groups are separate; US3 depends on the evaluation outputs from US1 and US2.
- **Polish (Phase 6)**: Depends on the desired user stories being complete.

### User Story Dependencies

- **US1 (P1)**: Depends on Foundational; no dependency on US2.
- **US2 (P1)**: Depends on Foundational; no dependency on US1.
- **US3 (P2)**: Depends on US1 and US2 so it can serialize both metric groups and render complete aggregates.

### Within Each User Story

- Tests should be written before or alongside implementation and must use deterministic fake judges/retrievers.
- Models and eligibility rules precede metric execution.
- Metric execution precedes aggregation.
- Aggregation precedes pipeline/CLI integration.
- Story checkpoint tests must pass before treating the story as complete.

## Parallel Execution Examples

### Foundational phase

```text
Parallel: T005 models.py
Parallel: T007 retrieval.py
Parallel: T008 answering.py
Parallel: T009-T010 evaluation.py
Parallel: T011 tracing.py
Then: T012 foundational tests
```

### User Story 1

```text
Parallel: T013-T016 tests in separate test modules/scopes
Then: T017-T019 evaluation.py implementation
Then: T020 pipeline.py orchestration
Then: T021 acceptance verification
```

### User Story 2

```text
Parallel: T022-T025 retrieval-focused tests
Then: T026-T029 evaluation.py retrieval metrics and aggregation
Then: T030 pipeline.py integration
Then: T031 acceptance verification
```

### User Story 3

```text
Parallel: T032 CLI tests
Parallel: T033 report serialization tests
Parallel: T034 partial-failure tests
Parallel: T035 offline-boundary tests
Then: T036-T040 pipeline/tracing/CLI implementation
Then: T041 quickstart validation
```

## Implementation Strategy

1. **MVP**: Complete Setup, Foundational, and US1. This produces generator-quality measurements and a deterministic local judge boundary.
2. **Retrieval coverage**: Complete US2 to add independent retrieval metrics and evidence-preserving diagnostics.
3. **Operational delivery**: Complete US3 to expose the full CLI/report/tracing workflow.
4. **Hardening**: Complete calibration, compatibility, security, and full-suite validation in Phase 6.

