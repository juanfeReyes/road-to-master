# Data Model: Portkey Chat Chunking

## EvaluationRunConfiguration

The resolved per-run configuration used by CLI parsing, startup validation, chunking setup, model construction, and report provenance.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `chat_model_source` | enum | yes | `local` or `portkey` |
| `chat_model_name` | string | yes | Non-empty runtime model identifier |
| `judge_model_name` | string | yes | Non-empty runtime model identifier |
| `chunking_strategy` | enum | yes | One of the supported evaluation chunking strategies |
| `chunking_settings` | object | conditional | Must satisfy the selected chunking strategy requirements |
| `portkey_connection` | object/null | conditional | Required only when `chat_model_source` is `portkey` |
| `selection_origin` | object | yes | Records whether each resolved value came from CLI, environment, or default resolution |
| `validation_status` | enum | yes | `valid` or `invalid` |
| `validation_messages` | list[string] | yes | Empty only when configuration is valid |

## PortkeyConnection

Non-secret runtime connection context required for Portkey-backed chat-model runs.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `base_url` | string | yes for portkey | Non-empty gateway URL |
| `api_key_present` | boolean | yes for portkey | True only when credentials are available at startup |
| `virtual_key_present` | boolean | no | Indicates whether an additional routing key is available |
| `provider_context` | string/null | no | Optional provider or routing label |

Sensitive values are never written to reports.

## ChatModelSelection

The evaluator-provided choice that determines how answer generation is performed.

| Field | Type | Description |
|---|---|---|
| `source` | enum | Whether answer generation uses a local or Portkey-backed model |
| `model_name` | string | The resolved chat-model identifier |
| `provided_by` | enum | `cli`, `environment`, or `default` |

## ChunkingSelection

The evaluator-provided choice that determines how source documents are split for retrieval.

| Field | Type | Description |
|---|---|---|
| `strategy` | enum | `section`, `fixed`, `recursive`, or `semantic` |
| `chunk_size` | integer/null | Required for fixed and recursive strategies |
| `chunk_overlap` | integer/null | Required for fixed and recursive strategies |
| `separators` | list[string] | Optional custom separators for recursive chunking |
| `breakpoint_threshold_type` | string/null | Optional semantic chunking threshold type |
| `breakpoint_threshold_amount` | number/null | Optional semantic chunking threshold amount |
| `embedding_model` | string/null | Required when the selected chunking strategy depends on embeddings |

## EvaluationReportContext

The existing evaluation report `configuration` object gains explicit run-context fields:

- `chat_model_source`
- `chat_model_name`
- `judge_model_name`
- `chunking_strategy`
- `chunking_settings`
- `portkey_context` when applicable

Metric definitions, aggregates, counts, and per-question result structures remain unchanged so reports stay comparable across local and Portkey-backed chat-model runs.

## StartupValidationResult

The pre-run validation outcome returned before question processing begins.

| Field | Type | Description |
|---|---|---|
| `status` | enum | `valid` or `invalid` |
| `messages` | list[string] | User-facing validation or availability messages |
| `resolved_config` | `EvaluationRunConfiguration/null` | Present only when validation succeeds |

A failed startup validation prevents evaluation execution and report success claims.