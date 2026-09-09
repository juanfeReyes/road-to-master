# Evaluation Model Selection CLI Contract

The existing `l1-assistant evaluate` command gains runtime model-source selection for local and Portkey-backed LangChain models. Existing evaluation dataset, metric, and report options remain compatible.

## Command

```text
uv run l1-assistant evaluate \
  --dataset path\to\eval.jsonl \
  --output path\to\report.json \
  --data-dir path\to\data \
  --db-dir path\to\var\chroma \
  --model-source local|portkey \
  --chat-model MODEL_NAME \
  --judge-model MODEL_NAME
```

## Inputs

### New model-selection inputs

- `--model-source SOURCE`: optional runtime source selector. Supported values are `local` and `portkey`. If omitted, the command uses the documented default resolution path.
- `--chat-model NAME`: optional runtime model identifier for the answer-generation role. When omitted, the command resolves the existing configured default.
- `--judge-model NAME`: optional runtime model identifier for the evaluation/judge role. When omitted, the command resolves the existing configured default.
- `--portkey-url URL`: optional explicit gateway URL for Portkey-backed runs when not already configured.
- `--portkey-provider LABEL`: optional non-secret provider or routing label recorded in report context for Portkey-backed runs.

### Existing inputs

All existing evaluation inputs remain available, including dataset path, output path, data directory, database directory, metric selection, question limits, thresholds, and tracing.

## Validation rules

1. If `--model-source local` is selected, the command must resolve valid local chat and judge model identifiers before question processing begins.
2. If `--model-source portkey` is selected, the command must resolve valid chat and judge model identifiers plus the required Portkey gateway configuration before question processing begins.
3. If model-source-specific required inputs are missing, the command exits non-zero with a clear corrective message.
4. If conflicting source inputs are provided, the command exits non-zero before evaluation starts.
5. The command must not silently fall back from the explicitly requested source to another source.

## Console output

Before question processing, the command prints:

1. The resolved model source.
2. The effective chat and judge model identifiers.
3. For Portkey-backed runs, non-secret gateway context sufficient to distinguish the run.

Validation and availability failures go to stderr and prevent a successful evaluation summary.

## Report output

The saved UTF-8 JSON report retains the existing evaluation schema and adds model-selection context under the report configuration section:

- `model_source`
- `selection_origin`
- `chat_model`
- `judge_model`
- `portkey_context` when applicable

Secret values such as API keys are never written to the report.

## Example commands

### Local models

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation-local.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --model-source local `
  --chat-model phi3:mini `
  --judge-model phi3:mini
```

### Portkey-backed models

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation-portkey.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --model-source portkey `
  --chat-model gpt-4o-mini `
  --judge-model gpt-4o-mini `
  --portkey-url https://gateway.example/v1 `
  --portkey-provider openai
```
