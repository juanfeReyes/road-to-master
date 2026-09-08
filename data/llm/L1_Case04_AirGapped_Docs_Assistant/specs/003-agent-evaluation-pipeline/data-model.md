# Data Model: Agent Evaluation Pipeline

## EvaluationDataset

Represents the versioned input JSONL file.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `dataset_id` | string | yes | Stable non-blank identifier |
| `dataset_version` | string | yes | Non-blank version label |
| `questions` | JSONL records | yes | At least one valid record |
| `metadata` | object | no | JSON-serializable |

Each record contains:

| Field | Type | Required | Validation |
|---|---|---:|---|
| `id` | string | yes | Unique within the dataset |
| `input` | string | yes | Non-blank question |
| `expected_output` | string | conditional | Required for correctness, contextual precision, or contextual recall |
| `expected_sources` | list[string] | no | Non-blank source IDs; duplicates removed |

## EvaluationConfiguration

Defines one run’s reproducible evaluation policy.

| Field | Type | Required | Description |
|---|---|---:|---|
| `metric_groups` | list[enum] | yes | `generator`, `retrieval`, or both |
| `metrics` | list[MetricDefinition] | yes | Selected metric names and thresholds |
| `top_k` | integer | yes | Retrieval window used for the run; greater than zero |
| `judge_model` | string | yes | Local evaluator identifier |
| `generator_model` | string | yes | Local answer model identifier |
| `dataset_hash` | string | yes | Content hash recorded at run start |
| `tracing_enabled` | boolean | yes | Whether local spans are captured |
| `max_questions` | integer/null | no | Positive limit when present |

## MetricDefinition

Defines a named metric and its eligibility.

| Field | Type | Description |
|---|---|---|
| `name` | string | Stable DeepEval or deterministic metric name |
| `group` | enum | `generator` or `retrieval` |
| `scale` | string | Documented `0.0` to `1.0` score scale |
| `threshold` | number | Configured pass threshold |
| `requires_reference` | boolean | Whether `expected_output` is required |
| `aggregation` | enum | Mean score plus eligible denominator |

## QuestionEvaluation

One ordered result for one dataset record.

| Field | Type | Description |
|---|---|---|
| `id` | string | Dataset question identifier |
| `input` | string | Original question |
| `actual_output` | string | Assistant answer, possibly empty |
| `retrieval_context` | list[RetrievedPassage] | Ordered passages supplied to the judge |
| `generator_metrics` | map[string, MetricResult] | Scores, pass state, and reasons |
| `retrieval_metrics` | map[string, MetricResult] | Scores, pass state, and reasons |
| `deterministic_checks` | map[string, CheckResult] | Non-LLM validation outcomes |
| `status` | enum | `evaluated`, `partial`, `failed`, or `skipped` |
| `error` | string/null | Actionable row-level failure |

## RetrievedPassage

Preserves retrieval evidence and ranking.

| Field | Type | Description |
|---|---|---|
| `rank` | integer | One-based retrieval position |
| `passage_id` | string | Stable passage identifier |
| `source_id` | string | Source document identifier |
| `text` | string | Passage text used as context |
| `retrieval_score` | number/null | Similarity score when available |

## MetricResult

| Field | Type | Description |
|---|---|---|
| `score` | number/null | `0.0` to `1.0`; null means unavailable |
| `passed` | boolean/null | Null when score is unavailable |
| `reason` | string | Judge or eligibility explanation |
| `available` | boolean | Distinguishes unavailable from zero |

## EvaluationReport

The persisted JSON report.

| Field | Type | Description |
|---|---|---|
| `schema_version` | string | Report compatibility version |
| `run_id` | string | Unique run identifier |
| `started_at` / `finished_at` | timestamp | Run timing |
| `dataset` | object | Dataset ID, version, and hash |
| `configuration` | object | EvaluationConfiguration |
| `metric_definitions` | list | Definitions and scales used |
| `aggregates` | map | Mean scores and eligible denominators |
| `counts` | object | Total, evaluated, partial, skipped, failed |
| `results` | list[QuestionEvaluation] | Ordered per-question results |
| `traces` | object/null | Local trace metadata when enabled |
