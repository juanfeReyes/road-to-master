# CLI Contract: Simple RAG Agent

## Commands

Run from the `l1-assistant` project:

```text
l1-assistant index [--data-dir PATH] [--db-dir PATH] [--chunk-strategy section|fixed]
l1-assistant ask "QUESTION" [--data-dir PATH] [--db-dir PATH]
```

`index` discovers Markdown files, rebuilds the local vector collection, and reports
the number of indexed files and passages. `ask` loads the existing collection,
retrieves evidence, generates a grounded answer, and prints the score.

## Output

Successful `ask` output MUST include:

- `Answer: ...`
- `Sources: ...` with source file names, or `Sources: none`
- `Score: N.NN/1.00` and a short score explanation, or `Score: unavailable`

Successful `index` output MUST include indexed file and passage counts.

## Errors

Invalid questions, missing data directories, missing indexes, unreadable documents,
and unavailable local models MUST produce a non-zero exit status and an actionable
message on stderr. The assistant MUST NOT claim success or fabricate a score after
these failures.
