import csv
from pathlib import Path

import pytest

from l1_assistant.models import GroundedResponse
from l1_assistant.chunking import config_from_args
from l1_assistant.config import Settings
from l1_assistant.pipeline import (
    QuestionRow,
    default_report_path,
    evaluate_questions,
    load_questions,
    EvaluationResult,
    resolve_runtime_model_config,
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


def test_resolve_runtime_model_config_defaults_to_local_with_chunking():
    settings = Settings.from_values(Path("../data"), Path("var/chroma"))
    chunking = config_from_args(strategy="recursive", chunk_size=512, chunk_overlap=64, separators="\n\n|\n| ")

    result = resolve_runtime_model_config(settings, chunking_config=chunking)

    assert result.status == "valid"
    assert result.resolved_config is not None
    assert result.resolved_config.model_source.source == "local"
    assert result.resolved_config.chunking_strategy == "recursive"
    assert result.resolved_config.chunking_settings["chunk_size"] == 512


def test_resolve_runtime_model_config_rejects_portkey_without_url_or_key():
    settings = Settings(
        data_dir=Path("../data"),
        db_dir=Path("var/chroma"),
        embedding_model="embed",
        chat_model="phi3:mini",
        judge_model="phi3:mini",
        model_source="local",
    )

    result = resolve_runtime_model_config(settings, model_source="portkey")

    assert result.status == "invalid"
    assert "gateway URL" in " ".join(result.messages)
    assert "API key" in " ".join(result.messages)


def test_resolve_runtime_model_config_rejects_semantic_chunking_without_embedding():
    settings = Settings.from_values(Path("../data"), Path("var/chroma"))
    chunking = config_from_args(strategy="semantic", embedding_model=None)

    result = resolve_runtime_model_config(settings, chunking_config=chunking)

    assert result.status == "invalid"
    assert "embedding model" in " ".join(result.messages)


def test_runtime_report_dict_includes_chunking_and_model_context():
    settings = Settings.from_values(Path("../data"), Path("var/chroma"))
    chunking = config_from_args(strategy="recursive", chunk_size=800, chunk_overlap=100)
    result = resolve_runtime_model_config(settings, chunking_config=chunking)

    payload = result.resolved_config.as_report_dict()

    assert payload["chat_model_source"] == "local"
    assert payload["chat_model_name"] == settings.chat_model
    assert payload["chunking_strategy"] == "recursive"
