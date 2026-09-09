# Research: Model-Selectable Evaluation

## Runtime model source selection

- **Decision**: Add explicit evaluation CLI parameters for model source selection and per-role model identifiers, with one runtime path for local models and one runtime path for Portkey-backed LangChain models.
- **Rationale**: The feature requires evaluators to switch model backends per run without editing environment files. Explicit runtime selection keeps runs reproducible and avoids hidden configuration drift.
- **Alternatives considered**: Environment-file-only switching was rejected because it requires file edits between runs. Automatic backend inference from model names was rejected because it is ambiguous and can silently choose the wrong source.

## Portkey integration approach

- **Decision**: Use the Portkey library through the existing LangChain-based model construction path so remote models are addressed through a single gateway-aware adapter.
- **Rationale**: The repository already uses LangChain abstractions for model access. Extending that path preserves a single evaluation workflow while allowing remote model routing when connectivity and credentials are available.
- **Alternatives considered**: Calling a remote gateway through a separate non-LangChain client would duplicate model setup logic and make local and remote evaluation paths diverge. Replacing the existing local model path entirely would break air-gapped usage.

## Configuration validation

- **Decision**: Normalize CLI inputs into a typed runtime model configuration before evaluation startup and validate required fields for the selected source, including generator and judge roles.
- **Rationale**: Early validation prevents long-running evaluations from failing after question processing begins and ensures invalid combinations are rejected consistently.
- **Alternatives considered**: Deferred validation during the first model call was rejected because it produces late, harder-to-diagnose failures. Allowing mixed unresolved parameters was rejected because it weakens reproducibility.

## Availability and failure behavior

- **Decision**: Perform startup checks for the selected source and fail fast with actionable messages when local models are unavailable or when Portkey connectivity or credentials are missing.
- **Rationale**: The specification requires clear pre-run validation and no silent fallback. Evaluators need to know immediately whether the requested source can be used.
- **Alternatives considered**: Silent fallback from Portkey-backed models to local models was rejected because it would misrepresent the run. Retrying indefinitely on remote failures was rejected because it obscures the root cause and delays feedback.

## Reporting and reproducibility

- **Decision**: Extend evaluation report configuration metadata to record the selected model source, per-role model identifiers, and any gateway-related context needed to distinguish local and Portkey-backed runs.
- **Rationale**: Reports must remain comparable across runs while making the active model source explicit. Capturing effective runtime configuration supports later analysis and regression comparison.
- **Alternatives considered**: Recording only the model names was rejected because the same identifier could refer to different backends. Recording source selection only in console output was rejected because it would not persist with the report.

## Dependency and packaging impact

- **Decision**: Add the Portkey and LangChain integration dependencies only in the existing Python project where evaluation models are constructed, and keep them optional at runtime for local-only runs.
- **Rationale**: The feature belongs in the current CLI application and should not introduce a separate service or package. Local-only evaluation must remain viable when remote connectivity is unavailable.
- **Alternatives considered**: A separate remote-evaluation package was rejected because it would fragment the CLI experience. Making remote dependencies mandatory for all runs was rejected because it adds unnecessary setup burden for air-gapped users.
