# Data Model: CSV Evaluation Input

## CsvEvaluationRow

One record read from the CSV source.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `question_id` | string | yes | Trimmed, non-blank, unique |
| `engineer_question` | string | yes | Trimmed for validation, non-blank |
| `expected_output` | string/null | no | Blank values normalize to null |
| `expected_sources` | string | no | Semicolon-delimited source IDs |
| extra columns | ignored metadata | no | Must not replace required fields |

The reader preserves the question/reference value contents except for boundary
whitespace normalization required by the contract.

## EvaluationRecord

The shared normalized record consumed by LangChain/DeepEval evaluation.

| Field | Type | Source |
|---|---|---|
| `id` | string | `question_id` |
| `input` | string | `engineer_question` |
| `expected_output` | string/null | `expected_output` |
| `expected_sources` | tuple[string] | Split, trim, deduplicate `expected_sources` |

## DatasetSource

Provenance captured before evaluation.

| Field | Type | Description |
|---|---|---|
| `path` | string | Original CSV or JSONL path |
| `format` | enum | `csv` or `jsonl` |
| `sha256` | string | Hash of raw input bytes |
| `row_count` | integer | Number of normalized records |

## CSVValidationError

Preflight failure that prevents a successful evaluation.

| Field | Type | Description |
|---|---|---|
| `message` | string | Human-readable problem |
| `line_number` | integer/null | CSV line when known |
| `field` | string/null | Header/field when known |

## EvaluationReport Extension

The existing report schema remains unchanged for metrics and results. Its dataset
provenance is extended with:

- `path`
- `format`
- `hash`
- `row_count`

CSV and JSONL reports use the same per-question result and aggregate structures.
