# Data Model: Evaluation Chunking Strategies

## ChunkingConfig

Normalized strategy selection shared by CLI, ingestion, retrieval, and reports.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `strategy` | enum | yes | `fixed`, `recursive`, or `semantic` |
| `chunk_size` | integer | no | Positive; default documented by CLI |
| `chunk_overlap` | integer | no | Non-negative and less than `chunk_size` |
| `separators` | tuple[string] | no | Ordered; recursive mode uses explicit defaults |
| `breakpoint_threshold_type` | enum | semantic only | `percentile`, `standard_deviation`, `interquartile`, or `gradient` |
| `breakpoint_threshold_amount` | number/null | semantic only | Valid amount for selected threshold mode |
| `embedding_model` | string/null | semantic only | Must resolve to a local model |

The serialized configuration contains effective values, not omitted CLI
defaults, so reports are reproducible.

## ChunkingStrategy

The splitter behavior selected for a run:

- `fixed`: fixed-size character windows with overlap.
- `recursive`: separator-prioritized splitting with character fallback.
- `semantic`: embedding-based sentence-group boundaries.

## ChunkedPassage

Existing `DocumentPassage` plus provenance:

| Field | Type | Description |
|---|---|---|
| `passage_id` | string | Stable source-plus-ordinal identifier |
| `source_id` | string | Relative source document ID |
| `heading` | string | Preserved source heading when available |
| `text` | string | Passage content |
| `position` | integer | Original passage order |
| `chunking_strategy` | enum | Effective strategy |
| `source_hash` | string | Source content hash |

## EvaluationReport Chunking Configuration

The existing report `configuration` object gains:

- `chunking_strategy`
- `chunking` containing all effective `ChunkingConfig` fields
- `embedding_model` when semantic mode is used

The existing metrics, aggregates, counts, and question result structures remain
unchanged.

## Index Identity

The retriever's persisted identity includes the source-content hash, embedding
model, and serialized `ChunkingConfig`. A request using a different strategy or
effective setting must not silently reuse an incompatible index.
