# Implementation Plan: Agent Evaluation Pipeline

**Branch**: `003-agent-evaluation-pipeline` | **Date**: 2026-09-06 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from [spec.md](./spec.md)

## Summary

Add an offline evaluation layer to the existing Python LangChain RAG assistant. The
pipeline will replay a versioned evaluation dataset through the existing answer and
retrieval flow, preserve ordered retrieval context, construct DeepEval test cases,
and calculate separate generator and retrieval metrics. DeepEval will use a local
judge adapter so no prompts, documents, answers, or traces leave the air-gapped
environment. Results will be printed as a concise summary and persisted as a
structured report containing configuration, metric definitions, aggregate values,
per-question scores, reasons, and failures.

## Technical Context

- **Language/Version**: Python 3.14 (project requirement)
- **Primary Dependencies**: Existing LangChain RAG components; add a pinned
  `deepeval` runtime dependency and use its `LLMTestCase`, RAG metrics, `GEval`,
  local model adapter, and local tracing APIs
- **Storage**: Versioned JSONL evaluation dataset; JSON report output; existing
  local Chroma index and local model artifacts
- **Testing**: Existing pytest suite; unit tests for dataset validation, test-case
  construction, metric availability, aggregation, report serialization, and
  failure isolation; integration tests with a fake local judge and retriever
- **Target Platform**: Offline local execution on project-supported Windows/Linux
  environments
- **Project Type**: Python CLI package
- **Performance Goals**: Evaluate a representative 100-question dataset without
  silently dropping rows; bound local judge concurrency and keep report overhead
  negligible relative to model inference
- **Constraints**: Air-gapped operation; no hosted DeepEval/telemetry dependency;
  preserve retrieval order and source IDs; distinguish zero from unavailable;
  avoid treating empty answers as passing; support partial failures
- **Scale/Scope**: One batch evaluation per invocation, initially targeting up to
  100 questions and the existing `top_k` retrieval window; batch replay is the
  CI-authoritative path and runtime traces are diagnostic evidence

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The repository constitution remains an uninstantiated template and defines no
enforceable project-specific gates. This plan follows the repository conventions:
it extends the existing `l1_assistant` package and CLI, reuses current answering
and retrieval behavior, keeps local/offline execution explicit, and adds focused
pytest coverage.

**Gate status**: PASS; no constitution violation or complexity exception is required.

## Project Structure

### Documentation (this feature)

```text
specs/003-agent-evaluation-pipeline/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── evaluation-cli.md
└── tasks.md              # Phase 2 output (/speckit-tasks)
```

### Source Code (repository root)

```text
l1-assistant/
├── pyproject.toml                  # add pinned DeepEval dependency
├── src/
│   └── l1_assistant/
│       ├── cli.py                  # add/extend evaluate command
│       ├── answering.py            # expose answer + ordered retrieval context
│       ├── evaluation.py           # DeepEval cases, metrics, judge adapter, aggregation
│       ├── models.py               # evaluation result/config/report models
│       ├── pipeline.py             # dataset orchestration and report persistence
│       └── tracing.py              # optional local DeepEval/LangChain spans
└── tests/
    ├── unit/
    │   ├── test_evaluation.py
    │   ├── test_pipeline.py
    │   └── ...
    └── integration/
        └── test_evaluation_cli.py
```

**Structure Decision**: Keep the feature in the current package. Separate metric
construction and aggregation from dataset/CLI orchestration, and make tracing an
optional diagnostic layer so batch evaluation remains deterministic and usable
when tracing is disabled.

## Design Decisions

1. Use JSONL rather than extending the current question CSV for evaluation input.
   Each line can carry `id`, `input`, `expected_output`, and `expected_sources`
   without lossy encoding; retain CSV support only as a future adapter.
2. Map generator quality to `AnswerRelevancyMetric`, `FaithfulnessMetric`, and a
   reference-based `GEval` correctness rubric. Map retrieval quality to
   `ContextualRelevancyMetric`, `ContextualPrecisionMetric`, and
   `ContextualRecallMetric`.
3. Wrap the approved local evaluator in a `DeepEvalBaseLLM` adapter implementing
   synchronous and asynchronous generation. Default metric execution to bounded
   or synchronous mode for constrained local hardware.
4. Add deterministic checks for blank answers, source citation/source-ID matches,
   and metric eligibility. These checks prevent DeepEval’s empty-answer edge case
   from becoming a false pass and make critical failures reproducible.
5. Preserve the ordered `DocumentPassage` list and serialize passage text plus
   source/rank metadata into `retrieval_context`; aggregate each metric only over
   eligible cases and record its denominator.
6. Instrument the answer, retrieval, and judge phases with local DeepEval spans or
   the LangChain callback handler when tracing is enabled. Persist local trace
   identifiers and component metadata, but do not require hosted tracing.
7. Store a report schema version, dataset hash, model identifiers, prompt/rubric
   versions, threshold configuration, and metric definitions so repeated runs are
   comparable.

## Complexity Tracking

No violations. The additional evaluator adapter and tracing module are required
to keep DeepEval local and to separate diagnostic traces from the authoritative
batch report; a single monolithic CLI implementation would make those contracts
harder to test and reuse.
