# Implementation Plan: Portkey Chat Chunking

**Branch**: `007-portkey-chat-chunking` | **Date**: 2026-09-08 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from [spec.md](./spec.md)

## Summary

Extend the evaluation workflow so each run can choose either a local Ollama chat model or a Portkey-backed LangChain chat model while preserving the existing chunking-strategy options. The design keeps the current offline-first evaluation flow, validates remote chat-model and chunking inputs before question processing, and records the resolved chat-model source and chunking context in reports for reproducible comparison.

## Technical Context

**Language/Version**: Python 3.14

**Primary Dependencies**: Existing LangChain packages, `langchain-ollama`, `deepeval`, and Portkey-compatible LangChain integration dependencies in the current CLI project

**Storage**: Local Markdown source files, local Chroma persistence, environment variables for optional defaults, and JSON evaluation reports

**Testing**: pytest unit tests, CLI integration tests, and evaluation report regression tests

**Target Platform**: Offline-first Windows/Linux local CLI execution, with optional connected execution for Portkey-backed chat-model runs

**Project Type**: Python CLI RAG application

**Performance Goals**: Preserve current local evaluation throughput for local chat-model runs; remote chat-model runs should fail fast on invalid configuration and keep report generation and question ordering unchanged

**Constraints**: Must not break air-gapped local evaluation; no silent fallback between local and Portkey-backed chat-model sources; CLI parameters override environment defaults for a single run; report schema must remain comparable across chunking strategies and chat-model sources

**Scale/Scope**: One evaluation run at a time, existing dataset sizes and report formats, two supported chat-model sources across current evaluation chunking strategies

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The repository constitution is an uninstantiated template and defines no enforceable project-specific gates. This plan stays within the existing Python CLI architecture, extends current LangChain-based model setup instead of introducing a new service, preserves offline local evaluation, and limits changes to configuration, validation, reporting, and focused tests.

**Gate status**: PASS; no constitution violation or complexity exception is required.

## Project Structure

### Documentation (this feature)

```text
specs/007-portkey-chat-chunking/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── chat-model-cli.md
└── tasks.md
```

### Source Code (repository root)

```text
l1-assistant/
├── pyproject.toml                         # Portkey/LangChain dependency updates
├── README.md                              # evaluation usage examples
├── src/l1_assistant/
│   ├── cli.py                             # evaluate command parameters and validation wiring
│   ├── config.py                          # environment-backed defaults
│   ├── evaluation.py                      # evaluation startup and report context
│   ├── models.py                          # runtime chat-model source/config data models
│   └── pipeline.py                        # model construction, chunking, and evaluation orchestration
└── tests/
    ├── integration/
    │   └── test_evaluation_cli.py
    └── unit/
        ├── test_evaluation.py
        └── test_pipeline.py
```

**Structure Decision**: Keep all changes inside the existing `l1-assistant` CLI project. Add normalized runtime chat-model configuration in `models.py`, parse and validate CLI inputs in `cli.py`, resolve defaults in `config.py`, construct the selected local or Portkey-backed LangChain chat model in `pipeline.py`, and persist chat-model source and chunking context through `evaluation.py` and report outputs.

## Design Decisions

1. Introduce an explicit runtime selector for the evaluation chat model so answer generation can use either a local Ollama model or a Portkey-backed remote model.
2. Keep chunking-strategy selection independent from chat-model source selection so all supported chunking strategies remain available for both local and remote chat-model runs.
3. Normalize CLI and environment inputs into a single typed runtime configuration object before evaluation startup so validation, model construction, and reporting use the same effective values.
4. Keep local chat-model behavior as the compatibility default for omitted source parameters to preserve air-gapped usage.
5. Route Portkey-backed chat-model creation through the existing LangChain-based model setup path rather than creating a separate evaluation workflow.
6. Validate source-specific and chunking-specific requirements before question processing, including required model identifiers, gateway settings, and chunking parameters.
7. Reject conflicting or incomplete source selections with actionable user-facing errors and no silent fallback.
8. Extend report configuration metadata to record chat-model source, model identifiers, and effective chunking selection while preserving existing metric and result structures.

## Complexity Tracking

No constitution violations or additional complexity exceptions were identified.
