from l1_assistant.models import DocumentPassage
from l1_assistant.scoring import score_response


def test_score_is_bounded_and_supports_grounded_answer():
    passages = [DocumentPassage("manual.md:0", "manual.md", "Safety", "Disconnect power first.", 0)]
    score, reason = score_response("What should I do first?", "Disconnect power first. (manual.md)", passages)
    assert 0 <= score <= 1
    assert reason


def test_empty_evidence_scores_zero():
    score, _ = score_response("unknown", "No information.", [])
    assert score == 0
