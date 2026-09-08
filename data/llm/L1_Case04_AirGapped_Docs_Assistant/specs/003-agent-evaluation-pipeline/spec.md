# Feature Specification: Agent Evaluation Pipeline

**Feature Branch**: `003-agent-evaluation-pipeline`
**Created**: 2026-09-06
**Status**: Draft
**Input**: User description: "Build an evaluation pipeline capable of measuring generator and retrieval metrics of the l1_asistant agent from langchain"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Evaluate Agent Responses (Priority: P1)

As an evaluator, I want to run a representative question dataset through the local assistant and measure the quality of its generated answers so that I can identify regressions and compare changes.

**Why this priority**: Generated-answer quality is the primary outcome users receive from the assistant.

**Independent Test**: Provide a fixture dataset containing questions, expected answers, and supporting evidence, run an evaluation, and verify that each eligible question receives generator metric results and an aggregate summary.

**Acceptance Scenarios**:

1. **Given** a valid evaluation dataset and an available local assistant, **When** the evaluation runs, **Then** it produces one result for each valid question with answer-quality measurements and any evaluation notes.
2. **Given** a generated answer that is unsupported or contradicts the expected answer, **When** generator metrics are calculated, **Then** the result reflects lower correctness and grounding quality rather than reporting a passing score.
3. **Given** a question whose expected answer is not available, **When** the evaluation runs, **Then** metrics that require a reference are marked unavailable with an explicit reason while reference-independent metrics remain eligible.

---

### User Story 2 - Evaluate Retrieval Quality (Priority: P1)

As an evaluator, I want to measure whether the assistant retrieves the right supporting passages so that I can improve the knowledge base and retrieval behavior independently of answer generation.

**Why this priority**: Poor retrieval can cause incorrect answers even when answer generation behaves correctly, so it must be observable as a separate quality dimension.

**Independent Test**: Provide questions with known relevant source passages, run an evaluation, and verify that retrieval hit, relevance, and ranking measurements are reported per question and in aggregate.

**Acceptance Scenarios**:

1. **Given** a dataset identifies one or more relevant source passages, **When** retrieval is evaluated, **Then** the report records whether a relevant passage was retrieved and how highly it was ranked.
2. **Given** retrieval returns irrelevant passages or no passages, **When** retrieval metrics are calculated, **Then** the report records the failure and does not treat the generated answer score as evidence of successful retrieval.
3. **Given** a question has multiple valid supporting passages, **When** retrieval is evaluated, **Then** the result recognizes any configured valid supporting passage and does not require one fixed passage ordering.

---

### User Story 3 - Review and Compare Evaluation Reports (Priority: P2)

As an evaluator, I want a human-readable and machine-readable report with aggregate metrics so that I can inspect individual failures and compare runs over time.

**Why this priority**: Durable, consistent reporting turns one-off measurements into a repeatable quality workflow.

**Independent Test**: Run the pipeline against a small fixture dataset, inspect the console summary and saved report, and verify that per-question results, aggregate metrics, configuration context, and failures are all represented.

**Acceptance Scenarios**:

1. **Given** an evaluation completes, **When** the report is rendered, **Then** it includes generator and retrieval aggregate metrics plus counts of evaluated, skipped, and failed cases.
2. **Given** two runs use the same dataset and evaluation configuration, **When** their reports are compared, **Then** the metric names, scales, and result fields are consistent.
3. **Given** some questions fail during evaluation, **When** the report is written, **Then** successful results remain available, failed rows include actionable error details, and the overall run status indicates partial failure.

### Edge Cases

- The evaluation dataset is missing, unreadable, malformed, empty, or missing required fields.
- A question is blank, duplicated, or has conflicting reference answers or source passages.
- The assistant cannot answer a question, returns no sources, or produces an empty answer.
- The evaluator cannot calculate a metric because required references or retrieved content are unavailable.
- A source passage is changed or removed between dataset creation and evaluation.
- The evaluation is run without network access and a required local model or artifact is unavailable.
- A report destination already exists or cannot be written.
- A dataset contains more questions than the configured evaluation limit or includes unsupported metric configuration.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The pipeline MUST accept a documented evaluation dataset containing a question and the reference information needed for the selected generator and retrieval metrics.
- **FR-002**: The pipeline MUST evaluate each valid question independently and preserve its dataset identifier and input order in the results.
- **FR-003**: The pipeline MUST invoke the existing local assistant for each eligible question and capture its answer and retrieved source passages.
- **FR-004**: The pipeline MUST calculate generator metrics that separately indicate answer correctness or relevance, support from retrieved evidence, and completeness when the required reference data is available.
- **FR-005**: The pipeline MUST calculate retrieval metrics that indicate relevant-passage hit rate, retrieved-passage relevance, and ranking quality when reference passages are available.
- **FR-006**: The pipeline MUST use documented, consistent numeric scales for every metric and MUST distinguish a zero score from an unavailable score.
- **FR-007**: The pipeline MUST produce per-question results containing the question identifier, answer, retrieved sources, each selected metric, metric availability or reason, and any evaluation error.
- **FR-008**: The pipeline MUST calculate aggregate generator and retrieval metrics across eligible questions and report the denominator used for each aggregate.
- **FR-009**: The pipeline MUST report counts for total, evaluated, skipped, unavailable-metric, and failed questions.
- **FR-010**: The pipeline MUST render a concise console summary and persist a structured report that can be used for later comparison.
- **FR-011**: The persisted report MUST include the dataset identity, evaluation timestamp, metric configuration, metric scales, aggregate results, and per-question results.
- **FR-012**: The pipeline MUST support explicit input and output locations and a repeatable configuration for selecting metric groups and evaluation limits.
- **FR-013**: The pipeline MUST operate without network access when the assistant's configured local models and evaluation resources are available.
- **FR-014**: The pipeline MUST validate input and configuration before evaluation and return a clear non-success result for invalid or empty inputs.
- **FR-015**: A failure for one question MUST be recorded with an actionable error and MUST NOT silently discard results for other questions.
- **FR-016**: The pipeline MUST avoid presenting unavailable or unevaluated metrics as passing scores.

### Key Entities

- **Evaluation Dataset**: A versioned collection of questions, expected answer information, and valid supporting passage references used as evaluation ground truth.
- **Evaluation Configuration**: The selected metric groups, scoring scales, limits, and runtime settings for one evaluation run.
- **Question Evaluation**: The answer, retrieved passages, generator metrics, retrieval metrics, availability reasons, and errors for one dataset question.
- **Metric Definition**: A named measurement with its purpose, scale, eligibility rules, and aggregation rule.
- **Evaluation Report**: The durable run record containing configuration context, per-question results, aggregate metrics, and outcome counts.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: For an evaluation dataset containing N valid questions, the report contains exactly N ordered question results, including explicit failure or unavailable status for every question that cannot be fully evaluated.
- **SC-002**: Every completed report contains separate aggregate summaries for generator and retrieval metrics, including the eligible-question denominator for each metric.
- **SC-003**: On a representative 100-question fixture with available references and local resources, at least 95% of questions complete with either populated metrics or an explicit, actionable unavailable/error status; no question is silently omitted.
- **SC-004**: On a fixture with known relevant passages, retrieval hit rate and ranking measurements correctly identify at least 90% of questions where a relevant passage appears in the configured retrieval window.
- **SC-005**: On a fixture with known supported and unsupported answers, generator grounding measurements distinguish the supported cases from unsupported cases in at least 90% of cases.
- **SC-006**: An evaluator can identify the dataset, configuration, metric scales, aggregate values, and any failed question using only the saved report.
- **SC-007**: A repeat evaluation of the same dataset and configuration produces the same report schema and metric definitions, enabling comparison between runs.
- **SC-008**: Invalid, empty, or unavailable-resource inputs produce a clear non-success result before claiming that an evaluation completed successfully.

## Assumptions

- The initial audience is a single evaluator running the assistant locally in an air-gapped environment.
- The assistant already exposes answer and retrieved-source behavior that the evaluation pipeline can observe.
- Evaluation datasets are prepared by maintainers and may contain reference answers, valid source identifiers, or both depending on the selected metrics.
- Metric definitions and scales are documented with the report and remain stable for a given report version.
- Evaluation resources required by the configured metrics are provisioned locally before network disconnection.
- The first release supports batch evaluation and saved reports; interactive dashboards, continuous monitoring, and remote experiment tracking are out of scope.
- Exact reference matching is not required when semantic equivalence can be established by the selected metric.
