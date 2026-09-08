# Implementation Plan: Evaluation Chunking Strategies

**Branch**: `005-evaluation-chunking-strategies` | **Date**: 2026-09-06 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from [spec.md](./spec.md)

## Summary

Add a shared LangChain-based chunking configuration to the local ingestion and
evaluation workflow. The CLI will accept `--chunking-strategy {fixed,recursive,semantic}`
and strategy-specific settings, build the requested splitter before retrieval,
and include the normalized strategy configuration in the evaluation report and
retriever/index identity. Fixed-size and recursive modes use
`langchain-text-splitters`; semantic mode uses the local embedding model with
LangChain's `SemanticChunker` from the optional `langchain-experimental`
dependency. Existing behavior remains the default when the option is omitted.

## Technical Context

- **Language/Version**: Python 3.14
- **Primary Dependencies**: Existing LangChain packages and
  `langchain-text-splitters`; add `langchain-experimental` for semantic
  chunking; existing local HuggingFace embeddings, Chroma, and pytest
- **Storage**: Local Markdown source files, Chroma persistence directory, and
  JSON evaluation reports
- **Testing**: Existing pytest suite with splitter unit tests, CLI parser tests,
  retriever integration tests, report provenance tests, and full regression tests
- **Target Platform**: Offline Windows/Linux local CLI execution
- **Project Type**: Python CLI RAG application
- **Performance Goals**: Fixed and recursive splitting should preserve current
  ingestion throughput; semantic splitting should process a representative
  document set without silently falling back and should preserve row/report order
- **Constraints**: No hosted service calls; semantic mode requires a local
  embedding model and `langchain-experimental`; explicit parameters and model
  settings must be recorded for reproducibility; existing default behavior must
  remain compatible
- **Scale/Scope**: One local index/evaluation run at a time, existing document
  corpus and evaluation limits, three supported strategies

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The repository constitution is an uninstantiated template and defines no
enforceable project-specific gates. This plan follows the existing CLI and
offline architecture, keeps strategy selection in shared ingestion/retrieval
code, uses existing LangChain conventions, and adds focused tests without a
new service or storage layer.

**Gate status**: PASS; no constitution violation or complexity exception is required.

## Project Structure

### Documentation (this feature)

```text
specs/005-evaluation-chunking-strategies/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── chunking-cli.md
└── tasks.md
```

### Source Code (repository root)

```text
l1-assistant/
├── pyproject.toml                         # semantic chunking dependency
├── src/l1_assistant/
│   ├── chunking.py                        # strategy configuration/splitter factory
│   ├── ingestion.py                       # document-to-passage conversion
│   ├── retrieval.py                       # strategy-aware index identity/build
│   ├── cli.py                             # evaluate/index options and report config
│   └── models.py                          # chunking configuration/provenance models
└── tests/
    ├── unit/
    │   ├── test_chunking.py
    │   └── test_pipeline.py
    └── integration/
        └── test_evaluation_cli.py
```

**Structure Decision**: Add a small `chunking.py` module for normalized
configuration, validation, and LangChain splitter construction. Keep
`ingestion.py` responsible for document discovery and passage metadata, and pass
the same configuration from `index`/`evaluate` into `LocalRetriever.build`.
The evaluator remains format-agnostic and only receives report configuration
metadata from the CLI.

## Design Decisions

1. Use `CharacterTextSplitter(separator="", chunk_size, chunk_overlap)` for
   fixed-size character chunks because it is deterministic and offline-friendly.
2. Use `RecursiveCharacterTextSplitter` with explicit separators
   (`\n\n`, `\n`, space, empty string) for recursive chunking so paragraph and
   word boundaries are preferred before character fallback.
3. Use LangChain Experimental `SemanticChunker` with the configured local
   embeddings for semantic chunking. Missing package/model resources fail
   explicitly; no fallback to another strategy is allowed.
4. Normalize a `ChunkingConfig` containing strategy, chunk size, overlap,
   separators, semantic breakpoint type/amount, and embedding model. Validate
   positive sizes, non-negative overlap, overlap less than size, supported
   breakpoint modes, and semantic prerequisites before indexing.
5. Preserve current default behavior by mapping an omitted option to the
   existing section-oriented ingestion behavior or its documented compatibility
   equivalent; report the effective strategy explicitly.
6. Include chunking configuration in evaluation `configuration` and retriever
   provenance, and include it in the index identity so an index built with one
   strategy is not silently reused for another.
7. Preserve source ID, heading, passage order, and passage IDs while applying
   LangChain splitters. Add splitter metadata only where it is reliable and
   avoid claiming exact semantic offsets.
