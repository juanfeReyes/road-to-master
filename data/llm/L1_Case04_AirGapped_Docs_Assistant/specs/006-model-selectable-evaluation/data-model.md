# Data Model: Model-Selectable Evaluation

## EvaluationModelSource

The runtime backend selected for one evaluation run.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `source` | enum | yes | `local` or `portkey` |
| `is_default` | boolean | yes | Indicates whether the source came from default resolution |
| `selection_origin` | enum | yes | `cli`, `environment`, or `default` |

## EvaluationModelRoleConfig

The effective model identifier assigned to one evaluation role.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `role` | enum | yes | `chat` or `judge` |
| `model_name` | string | yes | Non-empty runtime model identifier |
| `source` | enum | yes | Must match the selected evaluation model source |
| `provided_by` | enum | yes | `cli`, `environment`, or `default` |

## PortkeyConnectionConfig

Runtime connection details required only when the selected source is Portkey-backed.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `base_url` | string | yes for portkey | Non-empty gateway URL |
| `api_key_present` | boolean | yes for portkey | True only when credentials are available at startup |
| `virtual_key_present` | boolean | no | Indicates whether an additional gateway key or routing token is available |
| `provider_context` | string/null | no | Optional provider or routing label recorded for report context |

Sensitive values are never persisted in reports; only presence and non-secret context are recorded.

## EvaluationRuntimeModelConfig

Normalized runtime configuration shared by CLI parsing, startup validation, model construction, and report provenance.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `model_source` | `EvaluationModelSource` | yes | Must resolve before evaluation starts |
| `chat_model` | `EvaluationModelRoleConfig` | yes | Required for all runs |
| `judge_model` | `EvaluationModelRoleConfig` | yes | Required for all runs |
| `portkey` | `PortkeyConnectionConfig/null` | conditional | Required only for Portkey-backed runs |
| `validation_status` | enum | yes | `valid` or `invalid` |
| `validation_messages` | list[string] | yes | Empty only when configuration is valid |

## EvaluationReport Configuration Context

The existing evaluation report `configuration` object gains a model-selection section containing:

- `model_source`
- `selection_origin`
- `chat_model`
- `judge_model`
- `portkey_context` with non-secret gateway metadata when applicable

Existing metric definitions, aggregates, counts, and per-question result structures remain unchanged so reports stay comparable across sources.

## StartupValidationResult

The pre-run outcome returned before question processing begins.

| Field | Type | Description |
|---|---|---|
| `status` | enum | `valid` or `invalid` |
| `messages` | list[string] | User-facing validation or availability messages |
| `resolved_config` | `EvaluationRuntimeModelConfig/null` | Present only when validation succeeds |

A failed startup validation prevents evaluation execution and report success claims.
