from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path
from typing import Any, Literal


ChunkingStrategy = Literal["section", "fixed", "recursive", "semantic"]
ModelSource = Literal["local", "portkey"]
SelectionOrigin = Literal["cli", "environment", "default"]


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
    chunking_strategy: str = "section"
    source_hash: str = ""


@dataclass(frozen=True)
class ChunkingConfig:
    strategy: ChunkingStrategy = "section"
    chunk_size: int = 800
    chunk_overlap: int = 100
    separators: tuple[str, ...] = ("\n\n", "\n", " ", "")
    breakpoint_threshold_type: str = "percentile"
    breakpoint_threshold_amount: float = 95.0
    embedding_model: str | None = None

    def __post_init__(self) -> None:
        if self.strategy not in {"section", "fixed", "recursive", "semantic"}:
            raise ValueError("Unsupported chunking strategy.")
        if self.chunk_size <= 0:
            raise ValueError("chunk_size must be positive.")
        if self.chunk_overlap < 0 or self.chunk_overlap >= self.chunk_size:
            raise ValueError("chunk_overlap must be non-negative and less than chunk_size.")
        if not self.separators:
            raise ValueError("separators must contain at least one value.")
        if self.strategy == "semantic" and self.breakpoint_threshold_type not in {
            "percentile", "standard_deviation", "interquartile", "gradient"
        }:
            raise ValueError("Unsupported semantic breakpoint threshold type.")
        if self.breakpoint_threshold_amount <= 0:
            raise ValueError("breakpoint_threshold_amount must be positive.")
        if self.breakpoint_threshold_type == "percentile" and self.breakpoint_threshold_amount > 100:
            raise ValueError("Percentile breakpoint threshold must not exceed 100.")

    def as_dict(self) -> dict[str, Any]:
        return {
            "strategy": self.strategy,
            "chunk_size": self.chunk_size,
            "chunk_overlap": self.chunk_overlap,
            "separators": list(self.separators),
            "breakpoint_threshold_type": self.breakpoint_threshold_type,
            "breakpoint_threshold_amount": self.breakpoint_threshold_amount,
            "embedding_model": self.embedding_model,
        }


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


@dataclass(frozen=True)
class EvaluationRecord:
    id: str
    input: str
    expected_output: str | None = None
    expected_sources: tuple[str, ...] = ()


@dataclass(frozen=True)
class EvaluationModelSource:
    source: ModelSource
    is_default: bool
    selection_origin: SelectionOrigin


@dataclass(frozen=True)
class EvaluationModelRoleConfig:
    role: Literal["chat", "judge"]
    model_name: str
    source: ModelSource
    provided_by: SelectionOrigin


@dataclass(frozen=True)
class PortkeyConnectionConfig:
    base_url: str
    api_key_present: bool
    virtual_key_present: bool = False
    provider_context: str | None = None


@dataclass(frozen=True)
class EvaluationRuntimeModelConfig:
    model_source: EvaluationModelSource
    chat_model: EvaluationModelRoleConfig
    judge_model: EvaluationModelRoleConfig
    chunking_strategy: ChunkingStrategy = "section"
    chunking_settings: dict[str, Any] = field(default_factory=dict)
    portkey: PortkeyConnectionConfig | None = None
    validation_status: Literal["valid", "invalid"] = "valid"
    validation_messages: tuple[str, ...] = ()

    def as_report_dict(self) -> dict[str, Any]:
        payload = {
            "model_source": self.model_source.source,
            "chat_model_source": self.model_source.source,
            "selection_origin": self.model_source.selection_origin,
            "chat_model": self.chat_model.model_name,
            "chat_model_name": self.chat_model.model_name,
            "judge_model": self.judge_model.model_name,
            "judge_model_name": self.judge_model.model_name,
            "chunking_strategy": self.chunking_strategy,
            "chunking_settings": self.chunking_settings,
        }
        if self.portkey is not None:
            payload["portkey_context"] = {
                "base_url": self.portkey.base_url,
                "api_key_present": self.portkey.api_key_present,
                "virtual_key_present": self.portkey.virtual_key_present,
                "provider_context": self.portkey.provider_context,
            }
        return payload


@dataclass(frozen=True)
class StartupValidationResult:
    status: Literal["valid", "invalid"]
    messages: tuple[str, ...]
    resolved_config: EvaluationRuntimeModelConfig | None = None


@dataclass(frozen=True)
class MetricDefinition:
    name: str
    group: Literal["generator", "retrieval"]
    threshold: float = 0.7
    requires_reference: bool = False


@dataclass
class MetricResult:
    score: float | None = None
    passed: bool | None = None
    reason: str = ""
    available: bool = False


@dataclass(frozen=True)
class RetrievedPassage:
    rank: int
    passage_id: str
    source_id: str
    text: str
    retrieval_score: float | None = None


@dataclass
class QuestionEvaluation:
    id: str
    input: str
    actual_output: str = ""
    retrieval_context: list[RetrievedPassage] = field(default_factory=list)
    generator_metrics: dict[str, MetricResult] = field(default_factory=dict)
    retrieval_metrics: dict[str, MetricResult] = field(default_factory=dict)
    deterministic_checks: dict[str, MetricResult] = field(default_factory=dict)
    status: str = "evaluated"
    error: str | None = None


@dataclass
class EvaluationReport:
    schema_version: str
    run_id: str
    started_at: str
    finished_at: str
    dataset: dict[str, Any]
    configuration: dict[str, Any]
    metric_definitions: list[dict[str, Any]]
    aggregates: dict[str, dict[str, float | int | None]]
    counts: dict[str, int]
    results: list[QuestionEvaluation]
    traces: dict[str, Any] | None = None
