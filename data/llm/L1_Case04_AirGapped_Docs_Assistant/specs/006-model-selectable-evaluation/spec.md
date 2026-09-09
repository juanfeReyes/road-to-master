# Feature Specification: Model-Selectable Evaluation

**Feature Branch**: `[006-model-selectable-evaluation]`
**Created**: 2026-09-08
**Status**: Draft
**Input**: User description: "Allow the evaluation workflow to use local models and portkey + langchain models specified in cli parameters"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Choose Evaluation Model Source at Runtime (Priority: P1)

As an evaluator, I want to choose the model source through command-line parameters when I start an evaluation run so that I can use either fully local models or Portkey-backed LangChain models without changing project files.

**Why this priority**: Runtime model selection is the core user value because it removes manual reconfiguration and makes the evaluation workflow usable across different operating conditions.

**Independent Test**: Start one evaluation run with parameters selecting local models and another selecting Portkey-backed models, then verify both runs begin with the requested model source and record that choice in the results.

**Acceptance Scenarios**:

1. **Given** an evaluator provides command-line parameters for local models, **When** the evaluation starts, **Then** the workflow uses the specified local models for the run without requiring file edits.
2. **Given** an evaluator provides command-line parameters for Portkey-backed LangChain models, **When** the evaluation starts, **Then** the workflow uses the specified remote model settings for the run without requiring file edits.
3. **Given** an evaluator omits model-selection parameters, **When** the evaluation starts, **Then** the workflow uses the documented default behavior and reports which models were selected.

---

### User Story 2 - Validate Model Configuration Before Evaluation (Priority: P1)

As an evaluator, I want the workflow to validate the requested model configuration before processing questions so that I can correct invalid or incomplete parameters before a long evaluation run fails.

**Why this priority**: Early validation prevents wasted time and makes failures understandable in air-gapped and connected environments.

**Independent Test**: Start evaluation runs with incomplete, conflicting, and valid model parameters, then verify invalid runs stop before question processing and valid runs proceed.

**Acceptance Scenarios**:

1. **Given** required parameters for the selected model source are missing, **When** the evaluator starts the workflow, **Then** the workflow stops before evaluation and returns a clear explanation of what is missing.
2. **Given** the evaluator provides conflicting model-source parameters, **When** the workflow validates the request, **Then** it rejects the configuration with guidance on how to provide a valid combination.
3. **Given** the selected model source is valid but unavailable at runtime, **When** the workflow performs startup checks, **Then** it reports the availability problem clearly and does not claim the evaluation completed.

---

### User Story 3 - Preserve Comparable Reporting Across Model Sources (Priority: P2)

As an evaluator, I want reports to capture which model source and model names were used so that I can compare evaluation outcomes across local and Portkey-backed runs.

**Why this priority**: Comparable reporting turns model selection into a repeatable experiment rather than a one-off runtime choice.

**Independent Test**: Run evaluations with different model sources and inspect the saved reports to verify that each report includes the selected model source, model identifiers, and consistent metric fields.

**Acceptance Scenarios**:

1. **Given** an evaluation completes with local models, **When** the report is saved, **Then** it records that local models were used along with the selected model identifiers.
2. **Given** an evaluation completes with Portkey-backed models, **When** the report is saved, **Then** it records that Portkey-backed models were used along with the selected model identifiers.
3. **Given** two reports use different model sources, **When** an evaluator compares them, **Then** the model configuration context is explicit and the evaluation metrics remain comparable in structure.

### Edge Cases

- The evaluator provides a model source but omits one or more required model names.
- The evaluator provides both local-only and Portkey-only parameters in a combination that cannot be resolved unambiguously.
- The requested local model is not installed or cannot be reached from the evaluation environment.
- The requested Portkey-backed model configuration is missing gateway details or cannot be authenticated.
- The selected generator model is valid but the selected judge model is not, or vice versa.
- A report is generated from a partially failed run and must still record the requested model configuration.
- The evaluator reruns the same dataset with different model sources and expects report fields to remain stable.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The evaluation workflow MUST accept command-line parameters that let an evaluator choose the model source for a run without editing environment files.
- **FR-002**: The evaluation workflow MUST support selecting local models for all evaluation roles that currently depend on model configuration.
- **FR-003**: The evaluation workflow MUST support selecting Portkey-backed LangChain models for all evaluation roles that currently depend on model configuration.
- **FR-004**: The evaluation workflow MUST allow the evaluator to specify the model identifier for each configurable evaluation role through command-line parameters.
- **FR-005**: The workflow MUST define and document the default model-selection behavior when model parameters are omitted.
- **FR-006**: The workflow MUST validate the selected model source and required parameters before processing evaluation questions.
- **FR-007**: The workflow MUST reject incomplete or conflicting model-selection inputs with clear corrective guidance.
- **FR-008**: The workflow MUST perform startup availability checks for the selected model source and fail fast when the requested models cannot be used.
- **FR-009**: The workflow MUST use the selected model configuration only for the current run unless the evaluator explicitly chooses to persist it elsewhere.
- **FR-010**: The workflow MUST record the selected model source and model identifiers in the evaluation output and saved report.
- **FR-011**: The workflow MUST preserve the existing evaluation result structure so that reports remain comparable regardless of whether local or Portkey-backed models are used.
- **FR-012**: The workflow MUST provide clear user-facing messages indicating which model source and model identifiers are active for the run.
- **FR-013**: The workflow MUST continue to support fully local evaluation in air-gapped environments when local models are available.
- **FR-014**: The workflow MUST clearly indicate when Portkey-backed model selection requires connectivity or credentials that are unavailable.
- **FR-015**: The workflow MUST avoid silently falling back to a different model source than the one explicitly requested by the evaluator.

### Key Entities *(include if feature involves data)*

- **Model Source Selection**: The evaluator’s runtime choice of local models or Portkey-backed LangChain models for an evaluation run.
- **Model Role Configuration**: The set of model identifiers assigned to evaluation roles such as answer generation and judging for a single run.
- **Startup Validation Result**: The pre-run outcome that confirms whether the requested model source, parameters, and availability checks are valid.
- **Evaluation Report Context**: The portion of the saved output that records the selected model source and model identifiers alongside evaluation results.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Evaluators can start a valid run with either local or Portkey-backed model parameters in a single command without editing project configuration files.
- **SC-002**: 100% of runs with missing or conflicting model-selection inputs fail before question processing begins and return a clear corrective message.
- **SC-003**: 100% of completed evaluation reports identify the model source and model identifiers used for the run.
- **SC-004**: An evaluator can compare reports from local and Portkey-backed runs using the same report structure and metric fields without manual normalization.
- **SC-005**: When a requested model source is unavailable, the workflow reports the failure before evaluation results are produced and does not mislabel the run as successful.

## Assumptions

- The existing evaluation workflow already distinguishes configurable model roles such as the answering model and the judging model.
- Local models remain the primary option for air-gapped usage and should continue to work without network access.
- Portkey-backed LangChain models are an optional runtime path used when connectivity and credentials are available.
- The first release of this feature focuses on command-line driven selection and reporting, not on interactive configuration screens.
- Existing evaluation metrics and report semantics should remain unchanged apart from added model-configuration context.
