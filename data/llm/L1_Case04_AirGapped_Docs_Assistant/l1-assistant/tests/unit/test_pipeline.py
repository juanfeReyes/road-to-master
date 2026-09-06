import csv
from pathlib import Path

import pytest

from l1_assistant.models import GroundedResponse
from l1_assistant.pipeline import (
    QuestionRow,
    default_report_path,
    evaluate_questions,
    load_questions,
    EvaluationResult,
    write_report,
)


def test_load_questions_maps_dataset_columns_and_preserves_order(tmp_path: Path):
    input_path = tmp_path / "questions.csv"
    input_path.write_text(
        "question_id,engineer_question\nQ01,First question\nQ02,Second question\n",
        encoding="utf-8",
    )

    assert load_questions(input_path) == [
        QuestionRow("Q01", "First question", 2),
        QuestionRow("Q02", "Second question", 3),
    ]


def test_load_questions_rejects_blank_question(tmp_path: Path):
    input_path = tmp_path / "questions.csv"
    input_path.write_text("question_id,engineer_question\nQ01, \n", encoding="utf-8")

    with pytest.raises(ValueError, match="row 2"):
        load_questions(input_path)


def test_evaluate_questions_keeps_later_rows_after_row_failure(monkeypatch):
    calls = []

    def fake_answer(question, retriever, chat_model):
        calls.append(question)
        if question == "bad":
            raise RuntimeError("temporary failure")
        return GroundedResponse("answer", ["manual.md"], [], 0.75, "supported")

    monkeypatch.setattr("l1_assistant.pipeline.answer_question", fake_answer)
    results = evaluate_questions(
        [QuestionRow("Q01", "bad", 2), QuestionRow("Q02", "good", 3)],
        object(),
        "model",
    )

    assert calls == ["bad", "good"]
    assert results[0].error == "temporary failure"
    assert results[1].score == 0.75


def test_write_report_uses_stable_header_and_timestamped_default_path(tmp_path: Path):
    output_path = default_report_path(tmp_path)
    write_report([EvaluationResult(id="Q01", question="Question")], output_path)

    with output_path.open(newline="", encoding="utf-8") as handle:
        rows = list(csv.DictReader(handle))

    assert output_path.name.startswith("question-report-")
    assert rows[0]["id"] == "Q01"
    assert "score_reason" in rows[0]
