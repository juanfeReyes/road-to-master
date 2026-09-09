# Quickstart: Portkey Chat Chunking

## Prerequisites

1. Install the existing `l1-assistant` project environment.
2. Build or refresh the local retrieval index before evaluation runs.
3. Provision local Ollama models for local chat-model runs and any local judge-model runs.
4. For Portkey-backed chat-model runs, ensure gateway connectivity and required credentials are available in the runtime environment.
5. Prepare a valid evaluation dataset such as `tests/fixtures/evaluation.jsonl`.

## Validate a local chat-model run

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation-local.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --model-source local `
  --chat-model phi3:mini `
  --judge-model phi3:mini `
  --chunking-strategy recursive `
  --chunk-size 800 `
  --chunk-overlap 100 `
  --separators "`n`n|`n| |" `
  --metrics generator,retrieval
```

Validate that startup output identifies the local chat model and selected chunking strategy, and that the saved report records the same run context.

## Validate a Portkey-backed chat-model run

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation-portkey.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --model-source portkey `
  --chat-model @azure-openai-eus2/gpt-5.4 `
  --judge-model phi3:mini `
  --portkey-url https://portkeygateway.perficient.com/v1 `
  --chunking-strategy recursive `
  --chunk-size 800 `
  --chunk-overlap 100 `
  --separators "`n`n|`n| |" `
  --metrics generator,retrieval
```

Validate that startup output identifies the Portkey-backed chat model, preserves the selected chunking strategy, and saves report metadata that distinguishes the remote chat-model run from local runs.

## Validate failure behavior

1. Run with `--model-source portkey` and omit the required gateway configuration. Confirm the command exits before the first question is processed.
2. Run with a chunking strategy that requires additional settings and omit one of those settings. Confirm the command exits with a clear corrective message.
3. Run with a Portkey-backed chat model and an unavailable local judge model. Confirm the command exits before evaluation results are produced.

## Compare reports

1. Generate one local report and one Portkey-backed chat-model report using the same dataset and chunking strategy.
2. Confirm both reports preserve the same metric sections, aggregate structure, and per-question result layout.
3. Confirm each report includes explicit chat-model source and chunking-strategy context under the configuration section.

## Tests

```powershell
uv run pytest tests/unit/test_pipeline.py tests/unit/test_evaluation.py tests/integration/test_evaluation_cli.py
uv run pytest
```

See [chat-model-cli.md](contracts/chat-model-cli.md) and [data-model.md](data-model.md) for the complete runtime configuration and reporting contract.