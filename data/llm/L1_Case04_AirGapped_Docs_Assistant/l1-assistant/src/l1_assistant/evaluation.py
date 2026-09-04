import json
from pathlib import Path

from .answering import answer_question
from .retrieval import LocalRetriever


def evaluate_questions(questions: list[dict[str, str]], retriever: LocalRetriever,
                       chat_model: str, output: Path | None = None) -> list[dict]:
    results = []
    for question in questions:
        response = answer_question(question["question"], retriever, chat_model)
        results.append({
            "id": question.get("id", ""),
            "question": question["question"],
            "sources": response.sources,
            "score": response.score,
            "answer": response.answer,
        })
    if output:
        output.write_text(json.dumps(results, indent=2), encoding="utf-8")
    return results
