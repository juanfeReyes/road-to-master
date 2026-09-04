# Implementation Plan: Simple RAG Agent

**Branch**: `001-simple-rag-agent` | **Date**: 2026-09-03 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/001-simple-rag-agent/spec.md`

**Note**: This template is filled in by the `/speckit-plan` command; its definition describes the execution workflow.

## Summary

Build a local, air-gapped question-answering CLI over Markdown documentation. LangChain
will coordinate loading, chunking, retrieval, and answer generation; local embedding
models and a persistent local vector database will keep documents and queries on the
machine. Each answer will include source files and a transparent evidence-support score.

## Technical Context

**Language/Version**: Python >=3.14 (existing project requirement)

**Primary Dependencies**: LangChain core/community components; local embedding provider
(sentence-transformers through LangChain HuggingFace integration); local chat model
adapter (Ollama-compatible); Chroma persistent vector store; pytest for tests

**Storage**: Persistent local Chroma database under a project-local generated-data
directory; source Markdown remains authoritative in `data/`

**Testing**: pytest unit tests plus offline integration tests using deterministic local
test doubles; measured retrieval experiment over `data/engineer_questions.csv`

**Target Platform**: Local Windows/Linux/macOS workstation; no network required after
models and packages are provisioned

**Project Type**: Python library with a command-line application entry point

**Performance Goals**: Answer within 10 seconds for up to 100 Markdown files after the
index exists; report measurable retrieval hit rate and response quality

**Constraints**: Air-gapped execution; no document or query sent to a hosted service;
rebuild must remove stale vectors; unsupported questions must be declined; local model
and embedding artifacts must be explicitly configured and available

**Scale/Scope**: Single local user, three supplied Markdown documents initially, up to
100 documents in the first release, text questions only

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- PASS: The constitution is an unfilled template and defines no enforceable project
  principles or gates.
- PASS: The design remains a small single-project CLI/library and keeps the offline
  boundary explicit.

## Project Structure

### Documentation (this feature)

```text
specs/001-simple-rag-agent/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code (repository root)
```text
src/
└── l1_assistant/
    ├── __init__.py       # CLI entry point
    ├── config.py         # paths and model settings
    ├── ingestion.py      # Markdown discovery, chunking, indexing
    ├── retrieval.py      # vector search and source filtering
    ├── answering.py      # grounded answer generation
    ├── scoring.py        # evidence-support score
    └── cli.py            # user-facing commands

tests/
├── unit/
└── integration/

experiments/
└── retrieval_comparison.py
```

**Structure Decision**: Use the existing `l1-assistant` single Python project. Keep
feature modules under `l1-assistant/src/l1_assistant`, tests beside the project in
`tests`, and store generated vector data outside source control. The comparison
experiment is a reproducible script because measured chunking and local-vs-gateway
results are required deliverables; the gateway run is optional for the air-gapped
runtime but must be isolated from the production path.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | The design uses one project and direct LangChain/vector-store composition. |

## Constitution Check (Post-Design)

- PASS: Phase 1 preserves the offline boundary and does not introduce a hosted
  runtime dependency.
- PASS: The implementation remains one small Python project with explicit module
  boundaries and deterministic tests.
