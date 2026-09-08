# Feature Specification: Batch Question Scoring Pipeline

**Feature Branch**: `002-batch-question-scoring`
**Created**: 2026-09-04
**Status**: Draft
**Input**: User description: "Build a pipeline script in src folder that loads the csv questions in the file /data/engineer_questions.csv, uses each row a question for the cli.py script and builds a report of the score of each question loaded. Print the report in console and finally save the report as CSV file"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Score a Question Dataset (Priority: P1)

As an evaluator, I want to run one pipeline command against the engineering-question CSV so that every question receives an answer score from the existing assistant.

**Why this priority**: Processing the complete input dataset is the core value of the feature.

**Independent Test**: Provide a valid CSV containing multiple questions, run the pipeline, and verify that one score result is produced for every valid question.

**Acceptance Scenarios**:

1. **Given** a readable CSV with a question column and an available assistant index, **When** the pipeline is run, **Then** it evaluates each question exactly once.
2. **Given** a CSV with several valid question rows, **When** evaluation completes, **Then** the report contains a corresponding result for each row, including the original question and its score.

---

### User Story 2 - Review Results in the Console (Priority: P2)

As an evaluator, I want to see the generated report in the console so that I can quickly inspect the scores without opening another file.

**Why this priority**: Immediate visibility makes dataset evaluation useful in interactive and automated development workflows.

**Independent Test**: Run the pipeline with a small fixture CSV and verify that the console output presents each question and its score in a readable report.

**Acceptance Scenarios**:

1. **Given** evaluation results are available, **When** the pipeline finishes, **Then** it prints a report containing every evaluated question and its score.
2. **Given** a question cannot be evaluated, **When** the pipeline handles that row, **Then** the console identifies the row failure without hiding results for other rows.

---

### User Story 3 - Save Results for Later Analysis (Priority: P3)

As an evaluator, I want the report saved as a CSV so that I can archive, compare, or process the scores later.

**Why this priority**: A durable report supports repeatable evaluation beyond the current terminal session.

**Independent Test**: Run the pipeline with a fixture CSV and verify that the output file is created with the expected report columns and one row per input question.

**Acceptance Scenarios**:

1. **Given** the pipeline completes, **When** the output location is inspected, **Then** a CSV report exists with the original question, answer, score, and score explanation.
2. **Given** the input contains no valid question rows, **When** the pipeline is run, **Then** it reports the invalid or empty input and does not claim that a successful evaluation report was produced.

### Edge Cases

- The input file is missing, unreadable, or not valid CSV.
- The expected question column is missing or a question value is blank.
- The assistant returns an unavailable score or cannot evaluate one row.
- The input contains duplicate questions; each input row remains a distinct report row.
- The output file already exists; the pipeline replaces it only after a successful run.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The pipeline MUST load questions from the configured engineering-question CSV input, using one input row as one question.
- **FR-002**: The pipeline MUST evaluate each non-blank question through the existing assistant command behavior.
- **FR-003**: The pipeline MUST produce a report row for every valid input question, preserving the input row order.
- **FR-004**: Each report row MUST include the original question, the assistant answer, the score, and the score explanation.
- **FR-005**: The pipeline MUST print the complete report to the console after evaluation.
- **FR-006**: The pipeline MUST save the complete report as a CSV file after successful evaluation.
- **FR-007**: The pipeline MUST provide a clear error and non-success result when the input file cannot be read or has no valid questions.
- **FR-008**: A failure evaluating one question MUST be represented in that question's report result and MUST NOT silently discard other input rows.
- **FR-009**: The pipeline MUST support explicit input and output file locations so it can be used with the documented default dataset and test fixtures.

### Key Entities

- **Question Row**: One source CSV record containing a question and its original row position.
- **Evaluation Result**: The answer, score, score explanation, and any row-level error associated with one question.
- **Evaluation Report**: The ordered collection of evaluation results rendered to the console and persisted as CSV.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: For an input containing N valid question rows, the report contains exactly N result rows in the same order.
- **SC-002**: A normal run prints the full report and creates the CSV output without requiring manual intervention.
- **SC-003**: At least 95% of valid rows in a representative 100-question dataset complete with a populated score or an explicit unavailable/error result; no row is silently omitted.
- **SC-004**: Missing-input, malformed-input, and empty-question conditions produce a clear user-facing error and a non-success process result.
- **SC-005**: A reviewer can identify the question, answer, score, and score explanation for every completed row using only the console report or saved CSV.

## Assumptions

- The source dataset has a header identifying the question field; the default dataset is available at `data/engineer_questions.csv` relative to the project.
- The existing assistant has already been indexed or can perform its normal indexing behavior before answering questions.
- Scores use the assistant's existing score semantics and are not recalculated by the pipeline.
- The default report is written to a predictable CSV path under the project, while explicit input and output paths remain available for repeatable tests.
- Each input row is evaluated independently, and duplicate question text is allowed.
