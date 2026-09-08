# Research: Batch Question Scoring Pipeline

## Decision: Reuse the existing evaluation and answering flow

- **Decision**: The pipeline will construct the existing `LocalRetriever`, call the existing question-answering function for each row, and preserve the returned answer, score, sources, and score explanation.
- **Rationale**: This keeps score semantics in one place and ensures batch results match the existing `ask` command.
- **Alternatives considered**: Calling the CLI subprocess once per row was rejected because it duplicates initialization, complicates error handling, and makes structured result capture less reliable.

## Decision: Adapt the source CSV schema at the pipeline boundary

- **Decision**: Read `question_id` and `engineer_question` from `data/engineer_questions.csv`, then map them to the internal question fields expected by the evaluator.
- **Rationale**: The checked-in dataset uses `engineer_question`, while the existing evaluator expects a normalized `question` key. A boundary adapter avoids changing unrelated evaluator behavior.
- **Alternatives considered**: Renaming the input file column was rejected because it would alter the source dataset contract.

## Decision: Generate timestamped CSV output names

- **Decision**: Use a local timestamp with microsecond precision in a filesystem-safe format such as `YYYYMMDD-HHMMSS-ffffff` in the default report filename, with an explicit output path still accepted for tests and automation.
- **Rationale**: Each normal invocation gets a distinct, human-sortable report path without requiring cleanup or overwrite confirmation.
- **Alternatives considered**: Always overwriting a fixed report was rejected because it loses historical runs. A random UUID was rejected because it is less readable and less useful for sorting.

## Decision: Continue after row-level evaluation failures

- **Decision**: Catch only expected per-question evaluation failures, record an explicit error field for that row, and continue processing later rows. Input/read/validation failures remain fatal.
- **Rationale**: This satisfies the no-silent-omission requirement while distinguishing a bad batch from an individual answer failure.
- **Alternatives considered**: Aborting on the first question error was rejected because it produces no complete report for partially processable datasets.

## Decision: Keep processing sequential

- **Decision**: Evaluate rows in input order, one at a time.
- **Rationale**: It preserves deterministic report order and avoids competing local model/index workloads in the offline environment.
- **Alternatives considered**: Parallel execution was rejected for the initial feature because it complicates ordering, resource limits, and reproducible failure behavior.
