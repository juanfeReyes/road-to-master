# Required experiments

Two comparisons are deliverables, not optional extras. Both must be *measured* on
your own question set, with the numbers reported.

## 1. Chunking experiment

Build the pipeline at **at least two** chunk sizes or strategies, and score
retrieval on the questions in `data/engineer_questions.csv`.

Score retrieval quality, not just answer plausibility - for each question, did the
retrieved context actually contain what was needed to answer correctly? An answer
that happens to be right because the model already knew about turbines is not
evidence your retrieval works.

Report a table: strategy, retrieval hit rate, and the questions each strategy got
wrong. Then state which you chose and why. If one strategy is better on average but
worse on a specific class of question, say so - that is the interesting finding.

## 2. Local versus gateway

Build the same pipeline twice: once entirely locally (Ollama or equivalent, so no
document leaves the machine), and once through the Portkey gateway.

Report measured latency, cost, and answer quality for both, on the same questions.
Then make a recommendation for an air-gapped client, and be explicit about what the
local option costs you in quality - a recommendation that claims no downside is not
credible.

## Why this case requires it

The client cannot send documents to a cloud model, which makes the local option the
only viable one - but "it was the only option" is not an engineering
recommendation. The committee wants to know what it costs them, in numbers.
