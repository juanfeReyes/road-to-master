# Data Model: Simple RAG Agent

## SourceDocument

Represents one readable Markdown file.

- `source_id`: stable normalized relative path
- `path`: source file path
- `content`: decoded Markdown text
- `content_hash`: digest used to detect changes
- `modified_at`: source modification timestamp

Validation: extension must be `.md`; path must remain under the configured data
directory; unreadable files produce an actionable error and do not create an entity.

## DocumentPassage

Represents one retrievable portion of a source document.

- `passage_id`: deterministic source-plus-position identifier
- `source_id`: related `SourceDocument`
- `heading`: nearest Markdown heading when available
- `text`: passage content
- `position`: source order
- `embedding`: vector-store-managed representation

Validation: text must be non-empty; metadata must retain the source identifier and
heading where available.

## UserQuestion

- `text`: non-empty question
- `submitted_at`: request timestamp

Validation: whitespace-only questions are rejected with a user-facing error.

## GroundedResponse

- `answer`: generated or extractive answer
- `sources`: unique source IDs used
- `passages`: retrieved evidence references
- `score`: numeric 0-1 score or unavailable
- `score_reason`: concise explanation of score inputs

State transitions: retrieved -> answered -> scored -> printed. If no evidence is
retrieved, the answer state must be an insufficient-evidence response.

## IndexSnapshot

- `collection_id`: local vector collection name
- `source_ids`: documents included in the build
- `built_at`: build timestamp
- `embedding_model`: local model identifier
- `chunk_strategy`: strategy and parameters

Rebuilding creates a snapshot from the current corpus and removes stale source entries.
