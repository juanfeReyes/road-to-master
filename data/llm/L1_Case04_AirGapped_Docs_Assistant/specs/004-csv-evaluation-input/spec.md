# Feature Specification: CSV Evaluation Input

**Feature Branch**: `004-csv-evaluation-input`
**Created**: 2026-09-06
**Status**: Draft
**Input**: User description: "Allow the evaluate command from l1_assistant to process csv file from /data folder as questions to allow meassure the metrics from deepEval"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Evaluate Questions from a CSV (Priority: P1)

As an evaluator, I want the `evaluate` command to read questions from a CSV file in the data folder so that I can measure DeepEval generator and retrieval metrics without converting my existing question data to another format.

**Why this priority**: Reusing the existing question dataset is the core requested capability.

**Independent Test**: Provide a CSV with valid question rows, run `evaluate`, and verify that every row is evaluated and represented in the DeepEval report.

**Acceptance Scenarios**:

1. **Given** a readable CSV with the documented question column, **When** `evaluate` is run with that CSV, **Then** the command evaluates each non-blank question in input order.
2. **Given** a CSV row includes an identifier, expected answer, and expected sources, **When** evaluation runs, **Then** those values are passed to the corresponding DeepEval test case and retrieval checks.
3. **Given** a CSV contains only questions and no reference columns, **When** reference-independent metrics are selected, **Then** those metrics run and reference-dependent metrics are reported as unavailable with reasons.

---

### User Story 2 - Handle CSV Validation and Compatibility (Priority: P1)

As an evaluator, I want clear validation for malformed or incomplete CSV files so that incorrect datasets do not produce misleading metrics.

**Why this priority**: Evaluation results are only useful when input rows and references are interpreted reliably.

**Independent Test**: Run the command against valid, empty, malformed, missing-column, blank-row, duplicate-ID, and quoted-field fixtures and verify deterministic validation outcomes.

**Acceptance Scenarios**:

1. **Given** the CSV is missing or unreadable, **When** evaluation starts, **Then** the command returns a clear non-success result before claiming metrics were produced.
2. **Given** the required question column is missing or a question is blank, **When** the CSV is loaded, **Then** the command identifies the row or column problem and does not silently skip it.
3. **Given** question text contains commas, quotes, or newlines, **When** the CSV is loaded, **Then** the complete field value is preserved.
4. **Given** an existing JSONL dataset is supplied, **When** `evaluate` is run, **Then** existing JSONL behavior remains unchanged.

---

### User Story 3 - Configure CSV Evaluation from the CLI (Priority: P2)

As an evaluator, I want to select a CSV input explicitly or use the data-folder default while retaining existing metric and report options so that batch evaluation fits my workflow.

**Why this priority**: Explicit and predictable input selection prevents accidental evaluation of the wrong dataset.

**Independent Test**: Invoke `evaluate` with an explicit CSV path and with the documented default path, then verify identical metric configuration and report structure for the same data.

**Acceptance Scenarios**:

1. **Given** `--dataset` points to a CSV, **When** the command runs, **Then** it detects CSV format and loads it using the CSV contract.
2. **Given** no dataset path is supplied and the default CSV exists under the data folder, **When** the command runs, **Then** it evaluates that default file.
3. **Given** the output, metric, judge-model, limit, and trace options are supplied, **When** CSV evaluation runs, **Then** those options apply exactly as they do for JSONL evaluation.

### Edge Cases

- CSV uses UTF-8 with a BOM, quoted values, embedded commas, or embedded newlines.
- CSV has a header with alternate supported names or unexpected extra columns.
- The question column is missing, duplicated, blank, or contains whitespace-only values.
- An ID is missing or duplicated.
- Expected answer or expected-source fields are absent, blank, or contain multiple sources in a delimited value.
- The CSV is empty, malformed, unreadable, or contains rows after the configured evaluation limit.
- The default data-folder CSV does not exist while an explicit dataset path is omitted.
- A CSV row fails during assistant or DeepEval evaluation; later rows must remain represented.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The `evaluate` command MUST accept CSV files as evaluation datasets in addition to the existing JSONL format.
- **FR-002**: The CSV contract MUST document a required question column and supported optional columns for row ID, expected answer, and expected source identifiers.
- **FR-003**: The CSV loader MUST preserve CSV quoting, commas, embedded newlines, UTF-8 content, and input row order.
- **FR-004**: The CSV loader MUST map each valid row to the same evaluation record used by the existing DeepEval pipeline.
- **FR-005**: The loader MUST validate required columns, non-blank questions, ID uniqueness, and malformed records before evaluation.
- **FR-006**: Missing optional reference values MUST make only dependent metrics unavailable with an explicit reason; reference-independent metrics MUST remain eligible.
- **FR-007**: A row-level evaluation failure MUST retain the row ID and question, record an actionable error, and not discard later rows.
- **FR-008**: The command MUST detect the input format from the dataset extension or an explicit format option without changing existing JSONL behavior.
- **FR-009**: The command MUST support an explicit CSV dataset path and a documented default CSV path under the configured data folder.
- **FR-010**: Existing metric selection, thresholds, judge-model, max-question, trace, report, and offline behavior MUST apply equally to CSV input.
- **FR-011**: CSV-derived reports MUST contain the same schema, metric definitions, aggregates, denominators, and per-question result fields as JSONL-derived reports.
- **FR-012**: Missing, unreadable, empty, malformed, or invalid CSV input MUST return a clear non-success result and MUST NOT claim successful evaluation.

### Key Entities

- **CSV Evaluation Row**: One CSV record containing a question and optional evaluation references.
- **CSV Dataset Contract**: The documented column names, encoding, delimiter, quoting, and validation rules for CSV input.
- **Normalized Evaluation Record**: The common question representation consumed by DeepEval regardless of source format.
- **CSV Evaluation Report**: A standard evaluation report whose provenance identifies the CSV dataset and content hash.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: For a CSV containing N valid question rows, the report contains exactly N ordered question results, including explicit errors for rows that fail evaluation.
- **SC-002**: A CSV with quoted commas, embedded newlines, and UTF-8 text preserves 100% of question and reference field values after normalization.
- **SC-003**: At least 95% of valid rows in a representative 100-question CSV produce populated metrics or explicit unavailable/error results, with no silent omissions.
- **SC-004**: Invalid CSV inputs produce a clear non-success result before any successful evaluation report is claimed.
- **SC-005**: CSV and equivalent JSONL datasets produce the same normalized records and equivalent metric/report fields.
- **SC-006**: An evaluator can run CSV evaluation using the existing metric and report options without changing the command workflow.

## Assumptions

- The default CSV is `data/engineer_questions.csv` relative to the assistant project unless overridden by `--dataset` or configuration.
- The required question column is `engineer_question`; supported optional columns are `question_id`, `expected_output`, and `expected_sources`.
- Multiple expected sources may be represented as a semicolon-delimited value and are normalized into a list.
- CSV input is UTF-8 and uses the standard comma delimiter with a header row.
- CSV support is an input adapter only; DeepEval metrics and report schemas remain shared with JSONL evaluation.
- Existing JSONL evaluation remains the default behavior when a `.jsonl` path is supplied.
