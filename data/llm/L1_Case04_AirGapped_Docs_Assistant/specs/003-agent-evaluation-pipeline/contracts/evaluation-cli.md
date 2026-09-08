# Evaluation CLI Contract

The existing `l1-assistant` CLI gains an `evaluate` command. Existing `index`,
`ask`, and `pipeline` commands remain compatible.

## Command

```text
uv run l1-assistant evaluate \
  --dataset path\to\eval.jsonl \
  --output path\to\report.json \
  --data-dir path\to\data \
  --db-dir path\to\var\chroma \
  --metrics generator,retrieval \
  --judge-model local-judge \
  --max-questions 100 \
  --trace
```

All paths are explicit or use project-configured defaults. The command MUST work
without network access when local generator, embedding, and judge artifacts are
available.

## Inputs

- `--dataset PATH`: required JSONL evaluation dataset.
- `--output PATH`: required or deterministic default JSON report destination.
- `--data-dir PATH`: existing Markdown source directory.
- `--db-dir PATH`: existing local retrieval index.
- `--metrics generator,retrieval`: selected metric groups; default is both.
- `--judge-model NAME`: local evaluation model identifier, defaulting to the
  configured evaluator model.
- `--max-questions N`: optional positive evaluation limit.
- `--trace`: enable local DeepEval/LangChain diagnostic spans.
- `--threshold NAME=VALUE`: repeatable per-metric threshold override.

Invalid combinations, missing required reference fields for selected metrics, and
missing local resources MUST produce a non-zero exit status before claiming a
successful evaluation.

## Dataset format

Each non-blank line is one JSON object:

```json
{"id":"Q01","input":"What safety checks are required?","expected_output":"...","expected_sources":["manual.md"]}
```

`expected_output` is optional only when all selected metrics are reference-free.
`expected_sources` is optional, but retrieval source-ID deterministic checks are
unavailable when omitted.

## Console output

The command prints:

1. Dataset/run identity and selected metric groups.
2. One concise status line per question or a progress summary.
3. Generator and retrieval aggregate scores with eligible denominators.
4. Counts for total, evaluated, partial, skipped, and failed questions.
5. The saved report path.

Errors go to stderr. A partial run has a non-zero exit status unless an explicit
future policy allows partial success.

## Report output

The output is UTF-8 JSON matching [data-model.md](../data-model.md). It MUST
include schema version, dataset hash, model identifiers, metric definitions and
thresholds, aggregate denominators, ordered retrieval context, per-question
metric reasons, and row-level failures. A score of `0.0` is distinct from a null
unavailable score.
