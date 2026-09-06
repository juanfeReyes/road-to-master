from l1_assistant.evaluation import (
    aggregate_results,
    build_test_case,
    deterministic_checks,
    metric_definitions,
)
from l1_assistant.models import EvaluationRecord, MetricResult, QuestionEvaluation, RetrievedPassage


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
