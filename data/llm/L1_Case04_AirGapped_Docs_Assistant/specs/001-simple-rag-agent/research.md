# Research: Simple RAG Agent

## Decision: Use LangChain for the RAG pipeline

**Rationale**: The requested library provides standard components for Markdown loading,
text splitting, embeddings, vector retrieval, prompt composition, and local model
adapters. Keeping these concerns behind small project modules allows the CLI and tests
to remain independent of provider-specific details.

**Alternatives considered**: A hand-written TF-IDF/search pipeline would reduce
dependencies but would not satisfy the requested LangChain approach or provide a clear
path to local generative answers.

## Decision: Use a persistent local Chroma vector store

**Rationale**: Chroma supports persisted collections and similarity search without a
server or network dependency. A deterministic rebuild can recreate the collection from
the current Markdown corpus, ensuring removed files do not leave stale vectors.

**Alternatives considered**: FAISS is a reasonable local-only alternative but requires
more explicit persistence and metadata lifecycle handling. A hosted vector service is
incompatible with the air-gapped boundary.

## Decision: Use local HuggingFace/sentence-transformers embeddings

**Rationale**: Embedding artifacts can be provisioned once and loaded from a local
model path at runtime. This keeps document text, questions, and vectors on the machine
and avoids an external embedding API.

**Alternatives considered**: Ollama embeddings are also viable, but introduce a
separate local service requirement. A hosted embedding API violates the offline
requirement.

## Decision: Use an Ollama-compatible local chat model adapter

**Rationale**: A local model is needed to produce natural-language grounded answers
without transmitting documents. The adapter and model name remain configuration values
so deployments can select an available model.

**Alternatives considered**: A hosted LLM can be used only by a separately measured
gateway experiment; it is not part of the production air-gapped path. A
retrieval-only extractive answerer is a fallback for tests and unavailable models but
is less useful for normal questions.

## Decision: Score evidence support on a documented 0-1 scale

**Rationale**: The first release can provide a transparent, reproducible signal using
retrieval relevance, presence of citations, and answer overlap with retrieved evidence.
The score is explicitly an evaluation signal rather than a factuality guarantee.

**Alternatives considered**: An LLM judge would be more flexible but would add another
model dependency and make offline scoring less deterministic. Binary pass/fail would
not help compare responses.

## Decision: Compare chunking strategies on the supplied question set

**Rationale**: Section-aware chunks preserve safety constraints near procedures, while
fixed-size chunks provide a meaningful baseline. Both must report per-question
retrieval hits and wrong questions, as required by the existing experiment brief.

**Alternatives considered**: Semantic chunking is deferred because it adds complexity
before the two required baselines are measured.
