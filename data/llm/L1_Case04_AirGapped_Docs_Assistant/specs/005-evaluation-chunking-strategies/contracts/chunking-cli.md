# Chunking Strategy CLI Contract

The `index` and `evaluate` commands accept the same chunking options so the
retrieval index and evaluation run use compatible passages.

## Strategy selection

```powershell
uv run l1-assistant evaluate `
  --dataset ..\data\engineer_questions.csv `
  --output .\var\reports\semantic.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --chunking-strategy semantic `
  --embedding-model sentence-transformers/all-MiniLM-L6-v2
```

Supported values:

- `fixed`
- `recursive`
- `semantic`

The option may be omitted to preserve the existing default behavior.

## Shared options

- `--chunk-size INTEGER`: positive target character size.
- `--chunk-overlap INTEGER`: non-negative overlap smaller than chunk size.
- `--separators VALUE`: optional ordered separator list for recursive mode.
- `--embedding-model NAME`: local embedding model, required/used by semantic
  mode and otherwise preserving the configured embedding model.

Semantic options:

- `--breakpoint-threshold-type {percentile,standard_deviation,interquartile,gradient}`
- `--breakpoint-threshold-amount NUMBER`

Invalid combinations or unavailable local semantic dependencies produce a
non-zero result before a successful evaluation report is written. There is no
silent fallback.

## Report contract

The evaluation report retains the existing schema and adds to `configuration`:

```json
{
  "chunking_strategy": "recursive",
  "chunking": {
    "strategy": "recursive",
    "chunk_size": 800,
    "chunk_overlap": 100,
    "separators": ["\n\n", "\n", " ", ""]
  },
  "embedding_model": "sentence-transformers/all-MiniLM-L6-v2"
}
```

The report must identify the effective strategy and settings for every
successful run. Question order, metric definitions, aggregate fields, and
per-question result fields remain compatible with JSONL and CSV evaluation.
