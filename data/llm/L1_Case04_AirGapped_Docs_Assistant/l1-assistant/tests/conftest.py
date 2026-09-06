import sys
from pathlib import Path
import pytest

from l1_assistant.models import DocumentPassage, EvaluationRecord, GroundedResponse, RetrievedPassage

from l1_assistant.models import DocumentPassage, EvaluationRecord, GroundedResponse, RetrievedPassage


@pytest.fixture
def evaluation_record():
    return EvaluationRecord(
        id="Q01",
        input="What is documented?",
        expected_output="The document contains the answer.",
        expected_sources=("manual.md",),
    )


@pytest.fixture
def retrieved_passage():
    return RetrievedPassage(1, "manual-1", "manual.md", "The document contains the answer.")


@pytest.fixture
def grounded_response(retrieved_passage):
    passage = DocumentPassage(
        retrieved_passage.passage_id,
        retrieved_passage.source_id,
        "",
        retrieved_passage.text,
        0,
    )
    return GroundedResponse("The document contains the answer.", ["manual.md"], [passage], 1.0, "supported")
