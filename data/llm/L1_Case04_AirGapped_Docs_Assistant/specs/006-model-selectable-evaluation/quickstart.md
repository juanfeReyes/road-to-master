# Quickstart: Model-Selectable Evaluation

## Prerequisites

1. Install the existing `l1-assistant` project environment.
2. Provision local embedding, chat, and judge models for offline runs.
3. For Portkey-backed runs, ensure gateway connectivity and required credentials are available in the runtime environment.
4. Prepare a valid evaluation dataset such as `tests/fixtures/evaluation.jsonl`.

## Run a local evaluation

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation-local.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --model-source local `
  --chat-model phi3:mini `
  --judge-model phi3:mini `
  --metrics generator,retrieval
```

Validate that the console output identifies `local` as the model source and that the saved report records the selected chat and judge models.

## Run a Portkey-backed evaluation

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation-portkey.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --model-source portkey `
  --chat-model gpt-4o-mini `
  --judge-model gpt-4o-mini `
  --portkey-url https://portkeygateway.perficient.com/v1 `
  --metrics generator,retrieval
```

Validate that the console output identifies `portkey` as the model source and that the saved report records the selected source and model identifiers.

## Validate failure behavior

1. Run with `--model-source portkey` and omit required gateway configuration. Confirm the command exits before question processing with a clear corrective message.
2. Run with an unavailable local model. Confirm the command exits before evaluation results are produced.
3. Run with conflicting source-specific inputs. Confirm the command exits non-zero and does not silently switch sources.

## Compare reports

1. Open the local and Portkey-backed reports.
2. Confirm both reports preserve the same metric sections, aggregate structure, and per-question result layout.
3. Confirm each report includes explicit model-selection context under the configuration section.

## Tests

```powershell
uv run pytest tests/unit/test_evaluation.py tests/unit/test_pipeline.py tests/integration/test_evaluation_cli.py
uv run pytest
```

See [evaluation-model-cli.md](contracts/evaluation-model-cli.md) and [data-model.md](data-model.md) for the complete runtime configuration and reporting contract.
