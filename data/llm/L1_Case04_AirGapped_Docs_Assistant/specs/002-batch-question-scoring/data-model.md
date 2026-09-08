# Data Model: Batch Question Scoring Pipeline

## Question Row

Represents one source CSV record.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `id` | string | No | Preserve `question_id` when present; otherwise use an empty value |
| `question` | string | Yes | Derived from `engineer_question`; trim surrounding whitespace and reject blank values |
| `row_number` | integer | Yes | One-based source row number for diagnostics and traceability |

## Evaluation Result

Represents the result of evaluating one valid question.

| Field | Type | Required | Validation |
|---|---|---:|---|
| `id` | string | No | Same identifier as the source row |
| `question` | string | Yes | Original normalized question |
| `answer` | string | Yes | Assistant answer, or empty when evaluation fails |
| `sources` | string | Yes | Stable, CSV-safe representation of returned source names |
| `score` | decimal or empty | Yes | Existing score value; empty when unavailable |
| `score_reason` | string | Yes | Existing score explanation |
| `error` | string | Yes | Empty on success; explicit row-level failure otherwise |

## Evaluation Report

An ordered list of `Evaluation Result` records. Report rows retain source order, including rows with explicit evaluation errors. The CSV header is stable and includes:

`id,question,answer,sources,score,score_reason,error`

## Validation and Failure States

- Missing file, malformed CSV, missing `engineer_question` header, or zero valid questions: fatal input error; no successful report is advertised.
- Blank question row: invalid input row; report generation fails with a row-specific diagnostic rather than silently treating it as a question.
- Assistant evaluation exception: non-fatal row error captured in `error`; later rows continue.
- Existing output path: explicit paths may be replaced only after all rows have been evaluated and serialized successfully. Default timestamped paths avoid collisions.
