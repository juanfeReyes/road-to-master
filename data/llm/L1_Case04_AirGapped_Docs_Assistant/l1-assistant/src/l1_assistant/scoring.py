import re

from .models import DocumentPassage


def score_response(question: str, answer: str, passages: list[DocumentPassage]) -> tuple[float, str]:
    if not passages:
        return 0.0, "No supporting passages were retrieved."
    q = set(re.findall(r"\w+", question.lower()))
    evidence = set(re.findall(r"\w+", " ".join(p.text for p in passages).lower()))
    answer_terms = set(re.findall(r"\w+", answer.lower()))
    relevance = len(q & evidence) / max(len(q), 1)
    support = len(answer_terms & evidence) / max(len(answer_terms), 1)
    citation = 0.1 if any(p.source_id in answer for p in passages) else 0.0
    score = min(1.0, round(0.5 * relevance + 0.4 * support + citation, 2))
    return score, "Based on query overlap, evidence support, and source citation."
