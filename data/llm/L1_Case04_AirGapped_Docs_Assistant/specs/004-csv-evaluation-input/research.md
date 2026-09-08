# Research: CSV Evaluation Input

## CSV parsing

- **Decision**: Use `csv.DictReader` with `newline=""` and
  `encoding="utf-8-sig"`.
- **Rationale**: The standard library preserves quoted commas and embedded
  newlines when opened with `newline=""`; `utf-8-sig` accepts ordinary UTF-8 and
  transparently removes an optional BOM from the first header.
- **Alternatives considered**: `pandas.read_csv` adds an unnecessary dependency
  and type coercion. `csv.Sniffer` weakens the fixed comma-delimited contract.
  Manual comma splitting is incorrect for quoted or multiline fields.

## CSV schema and normalization

- **Decision**: Require `engineer_question` and `question_id`; accept optional
  `expected_output` and semicolon-delimited `expected_sources`. Normalize directly
  to the existing `EvaluationRecord`.
- **Rationale**: This matches the feature contract and current JSONL model while
  keeping DeepEval test-case construction in one place. Blank references become
  unavailable rather than empty expected answers.
- **Alternatives considered**: Generating IDs would be convenient but would
  weaken stable dataset identity and differ from JSONL validation. Expanding the
  older `QuestionRow` would couple CSV evaluation to the legacy scoring workflow.

## DeepEval and LangChain reuse

- **Decision**: Keep DeepEval metrics, local judge adapter, retrieved context,
  deterministic source checks, tracing, aggregation, and JSON report code shared.
- **Rationale**: The input adapter should affect only record loading. Shared
  `LLMTestCase` fields (`input`, `actual_output`, optional `expected_output`, and
  ordered `retrieval_context`) ensure CSV and JSONL have identical metric semantics.
- **Alternatives considered**: A CSV-specific evaluator would duplicate metric
  thresholds and risk inconsistent reports. Converting CSV to temporary JSONL
  would add unnecessary I/O and obscure raw-input hashing.

## Format dispatch and default path

- **Decision**: Detect `.csv` and `.jsonl` by suffix, reject unsupported suffixes
  clearly, and default omitted `--dataset` to `data/engineer_questions.csv`
  relative to the configured data directory.
- **Rationale**: Explicit suffix dispatch is deterministic and easy to test.
  The default supports the existing data layout without changing the explicit
  JSONL workflow.
- **Alternatives considered**: Content sniffing can misclassify malformed files.
  A mandatory path would not satisfy the requested data-folder workflow.

## Validation and failure handling

- **Decision**: Reject missing/unreadable/empty CSVs, missing required headers,
  blank questions, duplicate IDs, invalid row shapes, and malformed CSV before
  evaluation. Preserve row-level assistant/judge failures and continue.
- **Rationale**: Preflight errors prevent misleading reports; row-level failures
  preserve maximum diagnostic value and match the existing pipeline contract.
- **Alternatives considered**: Silently skipping bad rows violates report
  cardinality and hides dataset quality problems. Aborting on one model failure
  loses later valid results.

## Provenance and compatibility

- **Decision**: Hash raw bytes and retain the original dataset path/format in
  report configuration. Keep the existing legacy `pipeline --questions` CSV
  command behavior unchanged.
- **Rationale**: Raw hashes identify exact source files, while separate legacy
  behavior avoids regressions for users of the earlier CSV scoring feature.
- **Alternatives considered**: Hashing only normalized records cannot distinguish
  formatting or metadata changes. Replacing the legacy loader would be a broader,
  unnecessary behavior change.
