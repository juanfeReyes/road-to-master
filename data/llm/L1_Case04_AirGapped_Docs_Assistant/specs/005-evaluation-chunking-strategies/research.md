# Research: Evaluation Chunking Strategies

## LangChain splitter selection

- **Decision**: Use `CharacterTextSplitter` for fixed-size mode and
  `RecursiveCharacterTextSplitter` for recursive mode from
  `langchain-text-splitters`.
- **Rationale**: Both provide deterministic, local splitting with explicit size,
  overlap, and separator controls. Recursive splitting prefers paragraph,
  newline, and word boundaries before falling back to characters.
- **Alternatives considered**: Keeping the current hand-written slicing would
  not satisfy the LangChain integration requirement. Token splitting adds
  tokenizer assets and is not required for the requested character-oriented
  strategies.

## Semantic chunking

- **Decision**: Use `SemanticChunker` from `langchain-experimental` with the
  configured local embedding implementation.
- **Rationale**: It uses local embeddings and cosine-distance breakpoints to
  separate semantically different sentence groups while remaining compatible
  with the LangChain document model.
- **Alternatives considered**: A project-owned semantic algorithm would duplicate
  maintained LangChain behavior. Hosted semantic segmentation is incompatible
  with the air-gapped requirement.

## Strategy configuration and validation

- **Decision**: Normalize all CLI values into a typed configuration before
  document discovery/indexing. Support fixed, recursive, and semantic values,
  with explicit defaults for size, overlap, separators, and semantic breakpoint
  settings.
- **Rationale**: Early validation prevents silent fallback and makes reports
  reproducible. The same config can be used by indexing and evaluation.
- **Alternatives considered**: Passing raw CLI values through multiple layers
  increases drift and makes report provenance incomplete.

## Semantic prerequisites

- **Decision**: Require `langchain-experimental`, NumPy-compatible runtime
  dependencies, and locally available embedding model files for semantic mode.
  Raise an actionable error when any prerequisite is unavailable.
- **Rationale**: Semantic boundaries cannot be produced without embeddings.
  Falling back to recursive or fixed chunks would invalidate comparisons.
- **Alternatives considered**: Automatic fallback improves availability but
  produces misleading metrics and violates explicit strategy selection.

## Reproducibility and report provenance

- **Decision**: Include effective strategy settings, embedding model, source
  document hash, and chunking configuration in the index/report provenance.
- **Rationale**: Chunk boundaries depend on splitter parameters, source text,
  embedding model, and semantic threshold behavior. Reports must explain the
  retrieval context that produced their scores.
- **Alternatives considered**: Recording only the strategy name is insufficient
  to reproduce a run.

## Compatibility

- **Decision**: Preserve existing behavior when `--chunking-strategy` is omitted,
  and add the option to both `index` and `evaluate` so evaluation can rebuild
  the requested local index consistently.
- **Rationale**: The current evaluator builds/refreshes retrieval state before
  measuring metrics. Index and evaluate must use the same strategy to avoid
  mismatched passages.
- **Alternatives considered**: Adding the option only to evaluate would leave
  prebuilt indexes ambiguous and risk evaluating stale chunks.
