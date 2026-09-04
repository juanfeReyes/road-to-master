from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path


@dataclass(frozen=True)
class SourceDocument:
    source_id: str
    path: Path
    content: str
    content_hash: str
    modified_at: float


@dataclass(frozen=True)
class DocumentPassage:
    passage_id: str
    source_id: str
    heading: str
    text: str
    position: int


@dataclass(frozen=True)
class UserQuestion:
    text: str
    submitted_at: datetime = field(default_factory=datetime.now)


@dataclass
class GroundedResponse:
    answer: str
    sources: list[str]
    passages: list[DocumentPassage]
    score: float | None = None
    score_reason: str = ""


@dataclass(frozen=True)
class IndexSnapshot:
    collection_id: str
    source_ids: tuple[str, ...]
    built_at: datetime
    embedding_model: str
    chunk_strategy: str
