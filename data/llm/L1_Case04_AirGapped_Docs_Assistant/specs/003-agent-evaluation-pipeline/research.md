# Research: Agent Evaluation Pipeline

## DeepEval metric mapping

- **Decision**: Use `AnswerRelevancyMetric`, `FaithfulnessMetric`, and a custom
  `GEval` correctness rubric for generator quality. Use
  `ContextualRelevancyMetric`, `ContextualPrecisionMetric`, and
  `ContextualRecallMetric` for retrieval quality.
- **Rationale**: The first group separates whether an answer addresses the
  question, is supported by retrieved context, and matches a reference answer.
  The second group measures relevance, rank quality, and coverage of the
  retrieved context. DeepEval does not currently provide a directly exported
  answer-correctness metric equivalent to the requested reference-based measure;
  `GEval` supplies that rubric.
- **Alternatives considered**: A single faithfulness score would not detect
  incomplete retrieval or incorrect reference answers. Exact string matching is
  too brittle for natural-language answers, but remains useful as a supplementary
  deterministic check for structured outputs.

## Local judge and air-gapped operation

- **Decision**: Implement a `DeepEvalBaseLLM` adapter around the configured local
  evaluator model and disable hosted telemetry/tracing paths by default.
- **Rationale**: DeepEval’s RAG metrics are LLM-as-a-judge metrics. A generator
  model being local does not guarantee that the judge or telemetry is local.
  The adapter makes the network boundary explicit and testable.
- **Alternatives considered**: An OpenAI-compatible local endpoint is simpler
  when the deployment already exposes one. A direct adapter is retained as the
  project contract because it also supports non-OpenAI-compatible local runtimes.
  Deterministic-only evaluation is insufficient for semantic relevance and
  faithfulness.

## LangChain tracing integration

- **Decision**: Make local DeepEval tracing or its LangChain callback handler an
  optional diagnostic mode, while treating replayed `LLMTestCase` batch evaluation
  as authoritative for CI and comparison reports.
- **Rationale**: Traces capture retrieval order, source IDs, model calls, and
  intermediate failures during development. Replaying captured inputs, outputs,
  retrieval context, and references is more stable for regression testing than
  relying on a live trace flush.
- **Alternatives considered**: Hosted observability was rejected because it can
  violate air-gapped requirements. Trace-only evaluation was rejected because
  incomplete traces make denominators and reruns ambiguous.

## Test-case and dataset shape

- **Decision**: Use one JSON object per JSONL line with `id`, `input`,
  `expected_output` (optional for reference-free metrics), and
  `expected_sources` (optional list of source IDs). Persist actual output and
  ordered retrieval context only in the report.
- **Rationale**: JSONL supports streaming, stable row identity, optional
  reference fields, and richer future metadata without changing the CLI.
- **Alternatives considered**: The existing question CSV is adequate for simple
  batch questions but cannot represent expected answers and multiple valid
  source IDs safely. A single JSON document is less convenient for large
  datasets and partial recovery.

## Thresholds, aggregation, and failure semantics

- **Decision**: Configure thresholds per metric, aggregate only eligible cases,
  include denominators and unavailable reasons, and retain row-level failures.
  Default judge execution is bounded/synchronous for local hardware.
- **Rationale**: DeepEval defaults are not calibrated project policy, and
  asynchronous evaluation can overload a local model server. Explicit
  denominators prevent unavailable references from appearing as poor or passing
  scores.
- **Alternatives considered**: One global average hides metric-specific failures
  and reference coverage. Unbounded concurrency improves throughput only by
  risking resource exhaustion and nondeterministic local failures.

## Calibration and validation

- **Decision**: Add a small labeled calibration set covering supported,
  unsupported, ambiguous, unanswerable, multi-hop, and adversarial questions
  before finalizing thresholds.
- **Rationale**: LLM-judge scores are probabilistic. Thresholds should be
  compared with human judgments and monitored using score distributions and
  reasons, not only pass/fail aggregates.
- **Alternatives considered**: Adopting DeepEval’s default threshold without
  calibration is fast but risks false confidence, especially with a small local
  evaluator.
