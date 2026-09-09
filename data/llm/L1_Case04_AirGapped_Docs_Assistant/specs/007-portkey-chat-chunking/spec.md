# Feature Specification: Portkey Chat Chunking

**Feature Branch**: `[007-portkey-chat-chunking]`

**Created**: 2026-09-08

**Status**: Draft

**Input**: User description: "Enhance evaluate flow to allow use portkey remote model for the chat model with selecting chunking strategies"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Select Portkey Chat Evaluation Runs (Priority: P1)

As an evaluator, I want to run the evaluation flow with a Portkey-backed remote chat model so that I can compare remote generation behavior without changing project files or local defaults.

**Why this priority**: Remote chat-model selection is the core user value and enables the requested evaluation workflow.

**Independent Test**: Run the evaluation command with a Portkey-backed chat model and confirm the run starts with the requested remote chat model while preserving the rest of the evaluation flow.

**Acceptance Scenarios**:

1. **Given** an evaluator provides a Portkey-backed chat model and required connection settings, **When** the evaluation command starts, **Then** the run uses the requested remote chat model for answer generation.
2. **Given** an evaluator does not provide remote chat-model settings, **When** the evaluation command starts, **Then** the run continues to use the default local chat model behavior.

---

### User Story 2 - Combine Remote Chat Models with Chunking Choices (Priority: P1)

As an evaluator, I want to choose a chunking strategy while using a Portkey-backed remote chat model so that I can compare retrieval and generation outcomes across chunking approaches in one workflow.

**Why this priority**: The request explicitly combines remote chat-model selection with chunking strategy selection, making this necessary for meaningful evaluation comparisons.

**Independent Test**: Run the evaluation command with a Portkey-backed chat model and each supported chunking strategy, then confirm the selected strategy is applied and recorded for the run.

**Acceptance Scenarios**:

1. **Given** an evaluator selects a supported chunking strategy and a Portkey-backed remote chat model, **When** the evaluation command runs, **Then** the evaluation uses both the requested chunking strategy and the requested remote chat model in the same run.
2. **Given** an evaluator selects a chunking strategy that requires additional settings, **When** the evaluation command runs with a Portkey-backed remote chat model, **Then** the workflow honors the provided chunking settings without overriding the selected remote chat model.

---

### User Story 3 - Preserve Clear Run Context for Comparison (Priority: P2)

As an evaluator, I want evaluation output to clearly show whether the chat model was local or Portkey-backed and which chunking strategy was used so that I can compare runs confidently.

**Why this priority**: Clear provenance is required to compare results across local and remote chat-model runs and across chunking strategies.

**Independent Test**: Generate reports for local and Portkey-backed chat-model runs with different chunking strategies and confirm the run context is visible and comparable.

**Acceptance Scenarios**:

1. **Given** an evaluation run completes with a Portkey-backed remote chat model, **When** the report is saved, **Then** the report includes the resolved chat-model source and selected chunking strategy.
2. **Given** an evaluation run completes with a local chat model, **When** the report is saved, **Then** the report preserves the same report structure while identifying the local chat-model source and selected chunking strategy.

---

### Edge Cases

- What happens when an evaluator selects a Portkey-backed chat model but omits required remote connection settings?
- How does the workflow respond when a selected chunking strategy requires additional settings that are missing or invalid?
- What happens when a remote chat-model run is requested while the judge model remains local?
- How does the workflow behave when a Portkey-backed chat model is unavailable after the run has already selected a chunking strategy?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The evaluation workflow MUST allow evaluators to select the chat model source independently for each run.
- **FR-002**: The evaluation workflow MUST allow evaluators to provide a Portkey-backed remote chat model identifier for answer generation.
- **FR-003**: The evaluation workflow MUST preserve the existing local chat-model behavior when no remote chat-model selection is provided.
- **FR-004**: The evaluation workflow MUST allow evaluators to select any supported chunking strategy while using either a local or Portkey-backed chat model.
- **FR-005**: The evaluation workflow MUST apply chunking-strategy-specific settings provided for the run without changing the selected chat model source.
- **FR-006**: The evaluation workflow MUST validate required remote chat-model connection inputs before question processing begins.
- **FR-007**: The evaluation workflow MUST fail before evaluation starts when the selected chat model source or chunking configuration is incomplete or invalid.
- **FR-008**: The evaluation workflow MUST display the resolved chat model source, chat model identifier, judge model identifier, and chunking strategy at startup.
- **FR-009**: The evaluation workflow MUST persist the resolved chat model source and chunking strategy in saved evaluation output.
- **FR-010**: The evaluation workflow MUST preserve the existing evaluation result structure so that local and Portkey-backed runs remain comparable.

### Key Entities *(include if feature involves data)*

- **Evaluation Run Configuration**: The resolved per-run settings that determine chat model source, chat model identifier, judge model identifier, chunking strategy, and any required connection inputs.
- **Chat Model Selection**: The evaluator-provided choice that identifies whether answer generation uses a local or Portkey-backed remote model and which model identifier is used.
- **Chunking Selection**: The evaluator-provided choice that identifies the chunking strategy and any strategy-specific settings applied during the run.
- **Evaluation Report Context**: The saved run metadata that records the resolved chat model source, model identifiers, chunking strategy, and related provenance needed for comparison.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Evaluators can start a Portkey-backed chat-model evaluation run with a selected chunking strategy in one command without editing project files.
- **SC-002**: 100% of evaluation runs with missing required remote chat-model inputs or invalid chunking settings fail before the first question is processed.
- **SC-003**: 100% of completed evaluation reports identify the resolved chat model source and selected chunking strategy.
- **SC-004**: Evaluators can compare local and Portkey-backed chat-model runs using the same report structure across all supported chunking strategies.

## Assumptions

- Evaluators may choose a Portkey-backed remote chat model for answer generation while keeping the judge model selection unchanged unless they explicitly override it.
- Existing supported chunking strategies and their current settings remain in scope; this feature extends selection compatibility rather than introducing new chunking strategies.
- Required remote connection secrets are supplied through existing runtime environment practices rather than being embedded in commands or reports.
- The evaluation workflow continues to use the current report format, with only additional run-context metadata needed for comparison.