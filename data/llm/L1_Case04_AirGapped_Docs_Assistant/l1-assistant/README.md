Offline Markdown RAG assistant

## Usage

Provision the configured local embedding and chat model artifacts before disconnecting
from the network:

```powershell
uv run l1-assistant index --data-dir ..\data --db-dir .\var\chroma
uv run l1-assistant ask "What safety checks are required before changing the liner?" --data-dir ..\data --db-dir .\var\chroma
uv run l1-assistant pipeline --questions ..\data\engineer_questions.csv --data-dir ..\data --db-dir .\var\chroma
```

Answers include source filenames and an evidence-support score from `0.00` to `1.00`.
Questions without supporting passages are explicitly declined. Set `L1_EMBEDDING_MODEL`,
`L1_CHAT_MODEL`, `L1_DATA_DIR`, and `L1_DB_DIR` to configure local paths/models.
The pipeline prints one result per question and saves a timestamped CSV under
`var/reports/`. Use `--output path\to\report.csv` for a deterministic output path.

## Development

```powershell
uv run pytest
```

The retrieval experiment and its reporting requirements are documented in
`EXPERIMENT.md`.

## DeepEval evaluation

Run Local model:
```powershell
ollama run phi3:mini
```

Provision a local judge model before disconnecting from the network and set
`L1_JUDGE_MODEL` to its identifier. Evaluation uses DeepEval RAG metrics locally;
it does not require hosted tracing or telemetry:

```powershell
uv run l1-assistant evaluate `
  --dataset .\tests\fixtures\evaluation.jsonl `
  --output .\var\reports\evaluation.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --model-source local `
  --judge-model phi3:mini `
  --chat-model phi3:mini `
  --metrics generator,retrieval
```

Generator metrics include answer relevancy, faithfulness, and reference-based
correctness. Retrieval metrics include contextual relevancy, precision, and
recall. Reports preserve ordered passages, metric reasons, eligibility, and
aggregate denominators. Use `--trace` for local diagnostic spans only; do not
configure hosted DeepEval services in the air-gapped environment.

Calibrate thresholds with `tests/fixtures/evaluation-calibration.jsonl` against
human-reviewed outcomes before using metric pass/fail values as release gates.
Scores are probabilistic signals; inspect reasons and distributions as well as
aggregate means.

Evaluation also accepts CSV datasets. The required columns are
`question_id` and `engineer_question`; `expected_output` and
semicolon-delimited `expected_sources` are optional:

```csv
question_id,engineer_question,expected_output,expected_sources
Q01,"What checks are required?","Isolate and depressurize first.","maintenance.md;safety.md"
```

Use an explicit CSV or omit `--dataset` to use
`<data-dir>\engineer_questions.csv`:

```powershell
uv run l1-assistant evaluate `
  --dataset ..\data\engineer_questions.csv `
  --output .\var\reports\csv-evaluation.json `
  --data-dir ..\data `
  --db-dir .\var\chroma `
  --metrics generator,retrieval
```

CSV parsing supports UTF-8 with BOM, quoted commas, escaped quotes, embedded
newlines, and preserves input order. Invalid required fields, duplicate IDs, and
malformed rows fail before evaluation with a non-zero exit status. CSV reports
use the same DeepEval metrics and report schema as JSONL reports and include
dataset format, path, hash, and row count provenance.

Portkey-backed evaluation can be selected per run without changing local defaults:

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

Set `PORTKEY_API_KEY` or `PORT_KEY_KEY` in the environment before Portkey-backed runs.
The chat model can be Portkey-backed while the judge model remains local, and startup output reports the resolved model source, model identifiers, and chunking strategy.

### Evaluation chunking strategies

The `index` and `evaluate` commands accept `--chunking-strategy` with
`section` (the compatibility default), `fixed`, `recursive`, or `semantic`.
Fixed and recursive strategies accept `--chunk-size`, `--chunk-overlap`, and
pipe-separated `--separators`:

```powershell
uv run l1-assistant evaluate `
  --chunking-strategy recursive --chunk-size 800 --chunk-overlap 100 `
  --separators "`n`n|`n| |" `
  --model-source local `
  --chat-model phi3:mini `
  --judge-model phi3:mini `
  --output .\var\reports\recursive.json
```

```powershell
uv run l1-assistant evaluate `
  --chunking-strategy recursive --chunk-size 800 --chunk-overlap 100 `
  --separators "`n`n|`n| |" `
  --model-source portkey `
  --chat-model @azure-openai-eus2/gpt-5.4 `
  --judge-model @azure-openai-eus2/gpt-5.4 `
  --portkey-url https://portkeygateway.perficient.com/v1 `
  --output .\var\reports\recursive-portkey.json
```

Semantic chunking uses `langchain-experimental` and a local embedding model:

```powershell
uv run l1-assistant evaluate `
  --chunking-strategy semantic --embedding-model nomic-embed-text `
  --breakpoint-threshold-type percentile `
  --breakpoint-threshold-amount 95 `
  --output .\var\reports\semantic.json
```

```powershell
uv run l1-assistant evaluate `
  --chunking-strategy semantic --embedding-model nomic-embed-text `
  --breakpoint-threshold-type percentile `
  --breakpoint-threshold-amount 85 `
  --model-source portkey `
  --chat-model @azure-openai-eus2/gpt-5.4 `
  --judge-model @azure-openai-eus2/gpt-5.4 `
  --portkey-url https://portkeygateway.perficient.com/v1 `
  --output .\var\reports\semantic-portkey.json
```

Reports record the effective strategy, splitter settings, embedding model,
source hashes, and row-order-preserving dataset provenance. Semantic indexing
fails explicitly when its local dependency or model is unavailable; it does
not fall back to another strategy.

1. Install uv  
```sh
powershell -ExecutionPolicy ByPass -c "irm https://astral.sh/uv/install.ps1 | iex"
```

2. Install spec kit from GitHub  
```sh
uv tool install specify-cli --from git+https://github.com/github/spec-kit.git
```

3. Create project with uv  
```sh
 uv init l1-assistant
```

4. Setup specify   
```sh
specify init --here --integration copilot
```
