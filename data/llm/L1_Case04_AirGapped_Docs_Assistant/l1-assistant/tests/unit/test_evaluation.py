from pathlib import Path

import pytest

from l1_assistant.evaluation import (
    aggregate_results,
    build_judge,
    build_test_case,
    deterministic_checks,
    metric_definitions,
)
from l1_assistant.models import EvaluationRecord, MetricResult, QuestionEvaluation, RetrievedPassage
from l1_assistant.pipeline import resolve_runtime_model_config
from l1_assistant.chunking import config_from_args
from l1_assistant.config import Settings


def test_build_test_case_preserves_optional_reference(evaluation_record, retrieved_passage):
    case = build_test_case(evaluation_record, "actual", [retrieved_passage])
    assert case.input == evaluation_record.input
    assert case.expected_output == evaluation_record.expected_output
    assert case.retrieval_context == [retrieved_passage.text]


def test_missing_reference_marks_reference_metrics_ineligible():
    definitions = metric_definitions(("generator", "retrieval"))
    names = {item.name: item.requires_reference for item in definitions}
    assert names["answer_correctness"] is True
    assert names["answer_relevancy"] is False
    assert names["contextual_recall"] is True


def test_deterministic_checks_distinguish_empty_answer_and_source_hit(evaluation_record, retrieved_passage):
    checks = deterministic_checks(evaluation_record, "", [retrieved_passage])
    assert checks["non_empty_answer"].score == 0
    assert checks["non_empty_answer"].passed is False
    assert checks["expected_source_hit"].passed is True


def test_aggregate_results_uses_only_available_scores():
    result = QuestionEvaluation(
        id="Q01",
        input="question",
        generator_metrics={"answer_relevancy": MetricResult(0.8, True, "ok", True)},
    )
    definition = metric_definitions(("generator",))[0]
    aggregate = aggregate_results([result], [definition])
    assert aggregate["answer_relevancy"] == {"score": 0.8, "eligible": 1, "total": 1}


def test_build_judge_returns_local_judge_for_local_source():
    settings = Settings.from_values()
    runtime = resolve_runtime_model_config(settings, chunking_config=config_from_args())

    judge = build_judge(runtime.resolved_config)

    assert judge.get_model_name().startswith("local:")


def test_build_judge_requires_complete_portkey_configuration():
    settings = Settings(
        data_dir=Path("../data"),
        db_dir=Path("var/chroma"),
        embedding_model="embed",
        chat_model="phi3:mini",
        judge_model="phi3:mini",
        model_source="portkey",
        portkey_url="https://portkey.example/v1",
    )
    runtime = resolve_runtime_model_config(
        settings,
        model_source="portkey",
        portkey_url="https://portkey.example/v1",
        chunking_config=config_from_args(),
    )

    assert runtime.status == "invalid"
    assert runtime.resolved_config is None
    assert "API key" in " ".join(runtime.messages)
