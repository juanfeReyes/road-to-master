import json
from pathlib import Path

import pytest

from l1_assistant.cli import build_parser
from l1_assistant.pipeline import load_evaluation_dataset


def test_evaluate_parser_accepts_contract_options():
    args = build_parser().parse_args([
        "evaluate",
        "--dataset", "evaluation.jsonl",
        "--output", "report.json",
        "--metrics", "generator",
        "--judge-model", "local-judge",
        "--max-questions", "2",
        "--trace",
        "--threshold", "faithfulness=0.8",
    ])
    assert args.command == "evaluate"
    assert args.metrics == "generator"
    assert args.trace is True
    assert args.threshold == ["faithfulness=0.8"]
    assert args.dataset.name == "evaluation.jsonl"


def test_evaluate_parser_allows_default_dataset():
    args = build_parser().parse_args(["evaluate", "--output", "report.json"])
    assert args.dataset is None


def test_evaluate_parser_accepts_chunking_options():
    args = build_parser().parse_args([
        "evaluate", "--output", "report.json",
        "--chunking-strategy", "recursive",
        "--chunk-size", "512", "--chunk-overlap", "64",
        "--separators", "\\n\\n|\\n| ",
        "--embedding-model", "local-embeddings",
    ])
    assert args.chunking_strategy == "recursive"
    assert args.chunk_size == 512
    assert args.chunk_overlap == 64
    assert args.embedding_model == "local-embeddings"


def test_load_evaluation_dataset_preserves_hash_and_order(tmp_path: Path):
    dataset = tmp_path / "evaluation.jsonl"
    dataset.write_text(
        json.dumps({"id": "Q01", "input": "First"}) + "\n" +
        json.dumps({"id": "Q02", "input": "Second"}) + "\n",
        encoding="utf-8",
    )
    records, digest = load_evaluation_dataset(dataset)
    assert [record.id for record in records] == ["Q01", "Q02"]
    assert len(digest) == 64


def test_load_evaluation_dataset_reads_csv_and_normalizes_references(tmp_path: Path):
    dataset = tmp_path / "evaluation.csv"
    dataset.write_text(
        "question_id,engineer_question,expected_output,expected_sources\n"
        "Q01,\"Question, with a comma\",\"Expected answer\",\"a.md; b.md; a.md\"\n"
        "Q02,\"Question with\nan embedded newline\",,\n",
        encoding="utf-8",
        newline="",
    )

    records, digest = load_evaluation_dataset(dataset)

    assert [record.id for record in records] == ["Q01", "Q02"]
    assert records[0].expected_sources == ("a.md", "b.md")
    assert records[1].input == "Question with\nan embedded newline"
    assert len(digest) == 64


def test_load_evaluation_dataset_rejects_invalid_csv(tmp_path: Path):
    dataset = tmp_path / "invalid.csv"
    dataset.write_text("question_id,wrong_column\nQ01,Question\n", encoding="utf-8")

    with pytest.raises(ValueError, match="engineer_question"):
        load_evaluation_dataset(dataset)
