# Quickstart: Simple RAG Agent

## Prerequisites

- Python >=3.14 and the existing `l1-assistant` environment.
- Project dependencies installed from the package manifest.
- Local embedding model artifacts and an Ollama-compatible local chat model
  provisioned before entering the air-gapped environment.

## Build the local index

From `l1-assistant`:

```powershell
uv run l1-assistant index --data-dir ..\data --db-dir .\var\chroma
```

Expected result: the command reports all readable Markdown files and a non-zero
passage count. Re-running after adding, changing, or removing a Markdown file reports
an index reflecting the new corpus.

## Ask a grounded question

```powershell
uv run l1-assistant ask "What safety checks are required before changing the liner?" --data-dir ..\data --db-dir .\var\chroma
```

Expected result: output contains an answer, one or more source file names, and a
`Score: N.NN/1.00` line. A question unsupported by the corpus must instead produce an
insufficient-evidence answer with a low score.

## Validate behavior

```powershell
uv run pytest
```

The tests should cover Markdown discovery, rebuild behavior, source metadata,
unsupported questions, score formatting, and CLI failures.

## Run required measurements

Run the experiment over `..\data\engineer_questions.csv` with at least section-aware
and fixed-size chunking. Record retrieval hit rate and wrong question IDs. If a
networked gateway comparison is available outside the air-gapped runtime, run the
same question set through it and record latency, cost, and answer quality; report the
local recommendation and its measured quality tradeoff.
