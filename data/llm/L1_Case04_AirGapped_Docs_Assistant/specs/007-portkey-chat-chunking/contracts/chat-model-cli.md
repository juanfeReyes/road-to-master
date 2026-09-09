# Contract: Evaluation Chat Model CLI

## Command

`l1-assistant evaluate`

## Purpose

Allow evaluators to select a local Ollama chat model or a Portkey-backed remote chat model for answer generation while preserving existing chunking-strategy selection and evaluation reporting.

## Inputs

### New or clarified options

- `--model-source <local|portkey>`
  - Selects the chat-model source for the run.
  - Defaults to the existing local behavior when omitted.
- `--chat-model <model-id>`
  - Sets the chat-model identifier used for answer generation.
  - Required when overriding the default chat model.
- `--judge-model <model-id>`
  - Sets the judge-model identifier used for evaluation scoring.
  - May remain local even when the chat model is Portkey-backed.
- `--portkey-url <url>`
  - Required when `--model-source portkey` is selected.
- `--portkey-provider <label>`
  - Optional provider or routing label recorded for run context.
- `--chunking-strategy <section|fixed|recursive|semantic>`
  - Selects the retrieval chunking strategy for the run.
- `--chunk-size <integer>`
  - Required for chunking strategies that depend on explicit chunk size.
- `--chunk-overlap <integer>`
  - Required for chunking strategies that depend on explicit overlap.
- `--separators <pipe-delimited-values>`
  - Optional custom separators for recursive chunking.
- `--breakpoint-threshold-type <value>`
  - Optional semantic chunking threshold type.
- `--breakpoint-threshold-amount <number>`
  - Optional semantic chunking threshold amount.
- `--embedding-model <model-id>`
  - Required when the selected chunking strategy depends on embeddings.

## Validation rules

1. If `--model-source portkey` is selected, the run must fail before question processing unless required Portkey connection inputs are available.
2. If a selected chunking strategy requires additional settings, the run must fail before question processing when those settings are missing or invalid.
3. If `--model-source` is omitted, the run must preserve the existing local chat-model behavior.
4. The run must reject incomplete or conflicting chat-model source inputs without silently switching to another source.
5. The run must allow a Portkey-backed chat model to be combined with any supported chunking strategy.

## Startup output expectations

At startup, the command prints a concise summary including:

- resolved chat-model source
- resolved chat-model identifier
- resolved judge-model identifier
- selected chunking strategy
- Portkey gateway URL when applicable

## Report expectations

Saved evaluation output preserves the existing result structure and adds run-context metadata for:

- chat-model source
- chat-model identifier
- judge-model identifier
- chunking strategy
- chunking settings
- non-secret Portkey context when applicable