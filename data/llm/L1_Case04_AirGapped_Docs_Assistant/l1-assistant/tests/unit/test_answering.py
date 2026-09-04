from l1_assistant.answering import answer_question


class EmptyRetriever:
    def search(self, question):
        return []


def test_unsupported_question_is_explicit():
    response = answer_question("What is not documented?", EmptyRetriever(), "unused")
    assert "not provide enough information" in response.answer
    assert response.sources == []
    assert response.score == 0
