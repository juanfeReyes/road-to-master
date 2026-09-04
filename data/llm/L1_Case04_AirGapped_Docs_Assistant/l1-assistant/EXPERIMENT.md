# Retrieval and Model Comparison

Run `experiments/retrieval_comparison.py` against `data/engineer_questions.csv` and
record, for each strategy, the retrieval hit rate and wrong question IDs. Ground truth
must be reviewed from the document sections rather than inferred from answer wording.

The production path is local-only. A gateway comparison, if performed outside the
air-gapped environment, must use the same questions and report measured latency, cost,
and answer quality, including the local quality downside.
