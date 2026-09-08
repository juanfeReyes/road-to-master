# Feature Specification: Evaluation Chunking Strategies

**Feature Branch**: `005-evaluation-chunking-strategies`
**Created**: 2026-09-06
**Status**: Draft
**Input**: User description: "Allow the evaluate workflow to have the option to use different chunking strategies for the embeddings like: Fixed-size chunking, Recursive chunking and Semantic chunking. The user can select the chunking strategy from a command parameter"

## User Scenarios & Testing

### User Story 1 - Select Chunking for Evaluation (Priority: P1)

As an evaluator, I want to choose how source documents are divided before
retrieval so that I can compare DeepEval results under fixed-size, recursive, or
semantic chunking without changing the evaluation dataset or workflow.

**Why this priority**: Selecting the chunking strategy is the core requested
capability and directly affects retrieval context and measured metrics.

**Independent Test**: Run `evaluate` with each supported strategy against the
same local documents and evaluation questions, then verify that the selected
strategy is accepted, indexing/evaluation completes, and the report identifies
the strategy used.

**Acceptance Scenarios**:

1. **Given** a valid evaluation dataset and local source documents, **When** the
   evaluator selects fixed-size chunking, **Then** evaluation uses fixed-size
   passages and completes with the standard report.
2. **Given** a valid evaluation dataset and local source documents, **When** the
   evaluator selects recursive chunking, **Then** evaluation uses recursively
   structured passages and completes with the standard report.
3. **Given** a valid evaluation dataset and local source documents, **When** the
   evaluator selects semantic chunking, **Then** evaluation uses meaning-based
   passages and completes with the standard report.

---

### User Story 2 - Preserve Evaluation Comparability (Priority: P1)

As an evaluator, I want all other evaluation settings to remain unchanged when
I switch chunking strategies so that metric differences can be attributed to
chunking rather than accidental changes to the dataset, models, or thresholds.

**Why this priority**: Reliable comparison is essential for using generator and
retrieval metrics to select a strategy.

**Independent Test**: Run the same evaluation dataset with two or more chunking
strategies and verify that metric selection, question order, model settings,
thresholds, and report fields remain consistent while retrieval provenance
records the selected strategy.

**Acceptance Scenarios**:

1. **Given** identical dataset, model, metric, and threshold options, **When**
   only the chunking strategy changes, **Then** the report retains identical
   evaluation configuration except for the strategy and records results in the
   same question order.
2. **Given** an evaluation run uses a selected strategy, **When** the report is
   written, **Then** it identifies the strategy and enough chunking settings to
   reproduce the run.
3. **Given** no strategy is supplied, **When** evaluation runs, **Then** the
   existing default behavior is preserved.

---

### User Story 3 - Validate Strategy Selection (Priority: P2)

As an evaluator, I want invalid or unavailable chunking choices to fail clearly
before metrics are reported so that evaluation results are never mistaken for a
run using the requested strategy.

**Why this priority**: Clear validation prevents silent fallback and misleading
comparisons.

**Independent Test**: Invoke `evaluate` with an unsupported strategy, missing
local prerequisites, and valid strategies, and verify deterministic success or
actionable non-success behavior.

**Acceptance Scenarios**:

1. **Given** an unsupported strategy value, **When** `evaluate` is invoked,
   **Then** the command rejects it with the supported choices and a non-zero
   status.
2. **Given** semantic chunking cannot run because its required local resources
   are unavailable, **When** evaluation starts, **Then** the command reports the
   missing prerequisite and does not claim successful metrics.
3. **Given** indexing fails while applying a selected strategy, **When**
   evaluation starts, **Then** the command surfaces the failure and does not
   silently use another strategy.

### Edge Cases

- The selected strategy is applied to an empty or unreadable document folder.
- A document is shorter than the configured fixed-size chunk length.
- Recursive separators do not occur in a document and the fallback split is
  required.
- Semantic boundaries produce very small or very large passages.
- Existing indexes were built using a different strategy than the requested
  evaluation strategy.
- The evaluator repeats a run with the same strategy and settings.
- The evaluator omits the strategy and relies on the existing default.
- The selected strategy is supplied with an invalid size, overlap, threshold, or
  other strategy-specific option.

## Requirements

### Functional Requirements

- **FR-001**: The `evaluate` command MUST accept a command parameter for selecting
  the chunking strategy.
- **FR-002**: The command MUST support fixed-size, recursive, and semantic
  chunking strategies as distinct selectable values.
- **FR-003**: The selected strategy MUST be applied when preparing source
  passages used by retrieval during the evaluation run.
- **FR-004**: The command MUST preserve the existing evaluation dataset, metric
  selection, judge model, generator model, thresholds, tracing, output, and
  question-limit options when a strategy is selected.
- **FR-005**: The evaluation report MUST identify the selected chunking strategy
  and the effective strategy settings used for the run.
- **FR-006**: When no strategy is supplied, the command MUST preserve the
  currently configured default behavior.
- **FR-007**: Unsupported strategy values MUST be rejected before successful
  evaluation is reported and MUST list the supported choices.
- **FR-008**: Missing local resources or invalid strategy-specific settings MUST
  produce an actionable error and MUST NOT silently fall back to another
  strategy.
- **FR-009**: Re-running evaluation with the same source content, embedding
  settings, and chunking configuration MUST reuse or produce equivalent
  retrieval passages and provenance.
- **FR-010**: Evaluation results MUST preserve input question order and the
  existing per-question, aggregate, and metric report structures regardless of
  strategy.
- **FR-011**: Strategy selection MUST work in the offline execution environment
  without requiring hosted services.
- **FR-012**: Existing indexing and evaluation invocations that do not provide
  the new parameter MUST remain compatible.

### Key Entities

- **Chunking Strategy**: A named rule for dividing source documents into
  retrieval passages: fixed-size, recursive, or semantic.
- **Chunking Configuration**: The effective strategy and its settings, such as
  target size, overlap, separators, or semantic boundary controls.
- **Evaluation Run**: A metric execution over a dataset using one generator,
  retrieval configuration, and chunking configuration.
- **Chunk Provenance**: Metadata linking retrieved passages and evaluation
  reports to the source content, embedding settings, and chunking configuration.

## Success Criteria

### Measurable Outcomes

- **SC-001**: Evaluators can run the same valid dataset with each of the three
  supported strategies using only the documented command option.
- **SC-002**: 100% of completed evaluation reports identify the effective
  chunking strategy and settings used to produce their retrieval context.
- **SC-003**: For identical inputs and settings, repeated runs produce the same
  strategy selection and equivalent chunk provenance.
- **SC-004**: 100% of unsupported strategy values and unavailable prerequisites
  result in a clear non-success outcome with no successful-metrics claim.
- **SC-005**: Across strategy comparisons, 100% of input questions remain in the
  original order and represented by the standard result structure.
- **SC-006**: Existing evaluation commands without the new option continue to
  complete with their prior default behavior.

## Assumptions

- The existing local embedding and retrieval components remain the foundation
  for all three strategies.
- Fixed-size and recursive chunking use the project's existing document
  ingestion conventions unless strategy-specific settings are supplied.
- Semantic chunking uses locally available resources only; hosted semantic
  segmentation services are out of scope.
- The initial command option is one strategy value per evaluation invocation;
  comparing strategies is performed by separate runs.
- Existing default chunking behavior is preserved when the option is omitted.
- Strategy-specific defaults are documented and included in report provenance.
