# Quickstart: Evaluation Chunking Strategies

## Prerequisites

1. Install the project environment with the existing LangChain dependencies and
   the optional semantic chunking dependency.
2. Provision local embedding and chat/judge model files before disconnecting
   from the network.
3. Ensure the evaluation dataset is valid JSONL or CSV as documented by the
   existing evaluation workflow.

## Run fixed-size evaluation

```powershell
uv run l1-assistant evaluate `
  --dataset ..\data\engineer_questions.csv `
  --output .\var\reports\fixed.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --chunking-strategy fixed `
  --chunk-size 800 `
  --chunk-overlap 100 `
  --metrics generator,retrieval
```

## Run recursive evaluation

```powershell
uv run l1-assistant evaluate `
  --dataset ..\data\engineer_questions.csv `
  --output .\var\reports\recursive.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --chunking-strategy recursive `
  --chunk-size 800 `
  --chunk-overlap 100
```

## Run semantic evaluation

```powershell
uv run l1-assistant evaluate `
  --dataset ..\data\engineer_questions.csv `
  --output .\var\reports\semantic.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --chunking-strategy semantic `
  --embedding-model sentence-transformers/all-MiniLM-L6-v2 `
  --breakpoint-threshold-type percentile `
  --breakpoint-threshold-amount 95
```

## Validate results

1. Inspect each report's `configuration.chunking_strategy` and `configuration.chunking`.
2. Confirm the three runs preserve the same question order and metric fields.
3. Confirm changing only the strategy changes chunk provenance and retrieval
   context, not dataset identity or selected metric groups.
4. Run an unsupported strategy and confirm a non-zero error lists the supported
   choices.
5. Run semantic mode without local semantic dependencies/model files and confirm
   an actionable error with no successful report claim.
6. Run the existing evaluation command without `--chunking-strategy` and confirm
   prior default behavior.

## Tests

```powershell
uv run pytest tests/unit/test_chunking.py tests/integration/test_evaluation_cli.py
uv run pytest
```

See [chunking-cli.md](contracts/chunking-cli.md) and
[data-model.md](data-model.md) for the complete contract and provenance fields.
