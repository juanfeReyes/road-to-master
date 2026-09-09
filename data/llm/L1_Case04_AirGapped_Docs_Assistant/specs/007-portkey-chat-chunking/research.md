# Research: Portkey Chat Chunking

## Chat model source selection

- **Decision**: Allow the evaluation command to resolve the chat model independently as either a local Ollama model or a Portkey-backed remote model for each run.
- **Rationale**: The feature request focuses on answer generation model selection, so the runtime configuration must support chat-model source selection without forcing the same source choice for every role.
- **Alternatives considered**: Keeping a single shared source for both chat and judge roles was rejected because it prevents the requested mixed-mode scenario where remote chat generation can be evaluated while the judge remains local. Environment-only switching was rejected because it makes side-by-side comparisons harder and less reproducible.

## Portkey and LangChain integration path

- **Decision**: Use LangChain model construction as the common abstraction and route Portkey-backed chat models through a Portkey-compatible LangChain client.
- **Rationale**: The project already uses LangChain-based model access patterns. Extending that path keeps local and remote chat-model execution inside one evaluation workflow and avoids duplicating orchestration logic.
- **Alternatives considered**: Building a separate remote-only execution path was rejected because it would split validation, reporting, and chunking behavior. Replacing local Ollama support was rejected because the repository must remain offline-first.

## Chunking strategy compatibility

- **Decision**: Keep all existing chunking strategies available regardless of whether the chat model is local or Portkey-backed, and apply chunking configuration before model invocation.
- **Rationale**: Chunking affects retrieval preparation, not the source of the chat model. Preserving the same chunking options across local and remote chat-model runs enables direct comparison.
- **Alternatives considered**: Restricting remote chat-model runs to one chunking strategy was rejected because it would block the requested comparison workflow. Creating separate chunking defaults for remote runs was rejected because it would make comparisons less reliable.

## Validation and failure behavior

- **Decision**: Validate chat-model source requirements and chunking requirements together before the first evaluation question is processed.
- **Rationale**: The feature requires fail-fast behavior. Evaluators need immediate feedback when remote connection inputs or chunking settings are incomplete or invalid.
- **Alternatives considered**: Deferring validation until the first model call was rejected because it delays failure and can waste setup work. Silent fallback from Portkey-backed chat models to local models was rejected because it would misrepresent the run.

## Reporting and comparison context

- **Decision**: Persist chat-model source, chat-model identifier, judge-model identifier, and chunking strategy in the evaluation report configuration while preserving the existing result structure.
- **Rationale**: Evaluators need explicit provenance to compare local and Portkey-backed chat-model runs across chunking strategies without changing downstream report consumers.
- **Alternatives considered**: Recording only console output was rejected because it is not durable. Changing the per-question result schema was rejected because the feature only requires additional run context, not a new result format.

## Dependency impact

- **Decision**: Add only the LangChain and Portkey dependencies needed to construct Portkey-backed chat models inside the existing `l1-assistant` project.
- **Rationale**: The feature belongs in the current CLI application and should not introduce a separate package or service. Local-only runs must remain viable when remote connectivity is unavailable.
- **Alternatives considered**: A separate remote-evaluation package was rejected because it fragments the CLI workflow. Making remote connectivity mandatory for all runs was rejected because it breaks air-gapped usage.