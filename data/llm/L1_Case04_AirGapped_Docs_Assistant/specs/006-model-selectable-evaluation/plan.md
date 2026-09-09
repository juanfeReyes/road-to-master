# Implementation Plan: Model-Selectable Evaluation

**Branch**: `006-model-selectable-evaluation` | **Date**: 2026-09-08 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from [spec.md](./spec.md)

## Summary

Extend the evaluation workflow so each run can choose either local models or Portkey-backed LangChain models through CLI parameters. The design keeps the existing offline-first evaluation flow, adds a normalized runtime model configuration for generator and judge roles, validates source-specific requirements before question processing, and records the effective model source and identifiers in reports for reproducible comparison.

## Technical Context

**Language/Version**: Python 3.14

**Primary Dependencies**: Existing LangChain packages, `langchain-ollama`, `deepeval`, and add Portkey-compatible LangChain integration dependencies in the current CLI project

**Storage**: Local Markdown source files, local Chroma persistence, environment variables for optional defaults, and JSON evaluation reports

**Testing**: pytest unit tests, CLI integration tests, and evaluation report regression tests

**Target Platform**: Offline-first Windows/Linux local CLI execution, with optional connected execution for Portkey-backed runs

**Project Type**: Python CLI RAG application

**Performance Goals**: Preserve current local evaluation throughput for local runs; remote-backed runs should fail fast on invalid configuration and keep report generation and question ordering unchanged

**Constraints**: Must not break air-gapped local evaluation; no silent fallback between local and Portkey-backed sources; CLI parameters override environment defaults for a single run; report schema must remain comparable across model sources

**Scale/Scope**: One evaluation run at a time, existing dataset sizes and report formats, two supported model sources across current evaluation roles

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The repository constitution is an uninstantiated template and defines no enforceable project-specific gates. This plan stays within the existing Python CLI architecture, extends current LangChain-based model setup instead of introducing a new service, preserves offline local evaluation, and limits changes to configuration, validation, reporting, and focused tests.

**Gate status**: PASS; no constitution violation or complexity exception is required.

## Project Structure

### Documentation (this feature)

```text
specs/006-model-selectable-evaluation/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── evaluation-model-cli.md
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
│   ├── models.py                          # runtime model source/config data models
│   └── pipeline.py                        # model construction and evaluation orchestration
└── tests/
    ├── integration/
    │   └── test_evaluation_cli.py
    └── unit/
        ├── test_evaluation.py
        └── test_pipeline.py
```

**Structure Decision**: Keep all changes inside the existing `l1-assistant` CLI project. Add normalized runtime model-source configuration in `models.py`, parse and validate CLI inputs in `cli.py`, resolve defaults in `config.py`, construct the selected local or Portkey-backed LangChain models in `pipeline.py`, and persist model-source context through `evaluation.py` and report outputs.

## Design Decisions

1. Introduce an explicit runtime model-source selector for evaluation runs, with per-role model identifiers for answer generation and judging.
2. Normalize CLI and environment inputs into a single typed configuration object before evaluation startup so validation and reporting use the same effective values.
3. Keep local models as the compatibility default for omitted source parameters to preserve air-gapped behavior.
4. Route Portkey-backed model creation through the existing LangChain-based model setup path rather than creating a separate evaluation workflow.
5. Validate source-specific requirements before question processing, including required model identifiers, gateway settings, and source availability.
6. Reject conflicting or incomplete source selections with actionable user-facing errors and no silent fallback.
7. Extend report configuration metadata to record model source, per-role model identifiers, and effective runtime selection while preserving existing metric and result structures.
8. Update CLI documentation and tests so evaluators can run equivalent local and Portkey-backed evaluations with comparable outputs.
