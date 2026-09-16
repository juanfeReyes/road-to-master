import csv
import hashlib
import json
from dataclasses import asdict, dataclass
from datetime import datetime
from pathlib import Path
from typing import Iterable

from sympy import evaluate

from .answering import answer_question
from .evaluation import aggregate_results, build_judge, build_test_case, evaluate_case, metric_definitions
from .models import (
    EvaluationModelRoleConfig,
    EvaluationModelSource,
    EvaluationRecord,
    EvaluationReport,
    EvaluationRuntimeModelConfig,
    PortkeyConnectionConfig,
    RetrievedPassage,
    StartupValidationResult,
)
from .retrieval import LocalRetriever
from langchain_core.documents import Document


REPORT_FIELDS = ("id", "question", "answer", "sources", "score", "score_reason", "error")


@dataclass(frozen=True)
class QuestionRow:
    id: str
    question: str
    row_number: int


@dataclass
class EvaluationResult:
    id: str
    question: str
    answer: str = ""
    sources: str = ""
    score: float | None = None
    score_reason: str = ""
    error: str = ""


def load_questions(input_path: Path) -> list[QuestionRow]:
    try:
        with input_path.open("r", encoding="utf-8", newline="") as handle:
            reader = csv.DictReader(handle)
            if not reader.fieldnames or "engineer_question" not in reader.fieldnames:
                raise ValueError("Input CSV must contain an 'engineer_question' column.")
            rows = []
            for row_number, row in enumerate(reader, start=2):
                question = (row.get("engineer_question") or "").strip()
                if not question:
                    raise ValueError(f"Question at CSV row {row_number} is blank.")
                rows.append(QuestionRow(
                    id=(row.get("question_id") or "").strip(),
                    question=question,
                    row_number=row_number,
                ))
    except csv.Error as exc:
        raise ValueError(f"Could not parse input CSV {input_path}: {exc}") from exc
    if not rows:
        raise ValueError(f"Input CSV contains no questions: {input_path}")
    return rows


def default_report_path(report_dir: Path = Path("var/reports")) -> Path:
    timestamp = datetime.now().strftime("%Y%m%d-%H%M%S-%f")
    return report_dir / f"question-report-{timestamp}.csv"


def evaluate_questions(
    questions: Iterable[QuestionRow],
    retriever: LocalRetriever,
    chat_model: str,
) -> list[EvaluationResult]:
    results = []
    for question in questions:
        try:
            response = answer_question(question.question, retriever, chat_model)
            results.append(EvaluationResult(
                id=question.id,
                question=question.question,
                answer=response.answer,
                sources="; ".join(response.sources),
                score=response.score,
                score_reason=response.score_reason,
            ))
        except (OSError, ValueError, RuntimeError, ConnectionError) as exc:
            results.append(EvaluationResult(
                id=question.id,
                question=question.question,
                error=str(exc),
            ))
    return results


def write_report(results: Iterable[EvaluationResult], output_path: Path) -> None:
    output_path.parent.mkdir(parents=True, exist_ok=True)
    with output_path.open("w", encoding="utf-8", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=REPORT_FIELDS)
        writer.writeheader()
        for result in results:
            writer.writerow(asdict(result))


def format_report(results: Iterable[EvaluationResult]) -> str:
    lines = ["Question report"]
    for result in results:
        score = f"{result.score:.2f}/1.00" if result.score is not None else "unavailable"
        lines.extend((
            f"[{result.id or '-'}] {result.question}",
            f"  Score: {score}",
            # f"  Answer: {result.answer or 'unavailable'}",
            f"  Sources: {result.sources or 'none'}",
            # f"  Score explanation: {result.score_reason or 'none'}",
            f"  Error: {result.error or 'none'}",
        ))
    return "\n".join(lines)


def load_evaluation_dataset(input_path: Path, max_questions: int | None = None):
    if not input_path.is_file():
        raise FileNotFoundError(f"Evaluation dataset does not exist: {input_path}")
    suffix = input_path.suffix.lower()
    if suffix == ".csv":
        return _load_csv_evaluation_dataset(input_path, max_questions)
    if suffix != ".jsonl":
        raise ValueError(
            f"Unsupported evaluation dataset format '{input_path.suffix}'. "
            "Use a .csv or .jsonl file."
        )
    records: list[EvaluationRecord] = []
    seen: set[str] = set()
    raw_bytes = input_path.read_bytes()
    digest = hashlib.sha256(raw_bytes)
    for line_number, raw_line in enumerate(raw_bytes.splitlines(), start=1):
        try:
            line = raw_line.decode("utf-8").strip()
        except UnicodeDecodeError as exc:
            raise ValueError(f"Evaluation dataset is not valid UTF-8 at line {line_number}.") from exc
        if not line:
            continue
        try:
            value = json.loads(line)
        except json.JSONDecodeError as exc:
            raise ValueError(f"Invalid JSON at line {line_number}: {exc.msg}") from exc
        if not isinstance(value, dict):
            raise ValueError(f"Evaluation row {line_number} must be an object.")
        row_id = str(value.get("id", "")).strip()
        question = str(value.get("input", "")).strip()
        if not row_id or not question:
            raise ValueError(f"Evaluation row {line_number} requires non-blank id and input.")
        if row_id in seen:
            raise ValueError(f"Duplicate evaluation id: {row_id}")
        expected = value.get("expected_output")
        if expected is not None:
            expected = str(expected).strip()
            if not expected:
                expected = None
        sources = tuple(dict.fromkeys(
            str(source).strip() for source in value.get("expected_sources", []) if str(source).strip()
        ))
        seen.add(row_id)
        records.append(EvaluationRecord(row_id, question, expected, sources))
        if max_questions is not None and len(records) >= max_questions:
            break
    if not records:
        raise ValueError(f"Evaluation dataset contains no questions: {input_path}")
    return records, digest.hexdigest()


def _normalize_expected_sources(value: object) -> tuple[str, ...]:
    if value is None:
        return ()
    if isinstance(value, str):
        values = value.split(";")
    else:
        values = value
    return tuple(dict.fromkeys(
        str(source).strip() for source in values if str(source).strip()
    ))


def _load_csv_evaluation_dataset(input_path: Path, max_questions: int | None):
    records: list[EvaluationRecord] = []
    seen: set[str] = set()
    raw_bytes = input_path.read_bytes()
    digest = hashlib.sha256(raw_bytes)
    try:
        with input_path.open("r", encoding="utf-8-sig", newline="") as handle:
            reader = csv.DictReader(handle)
            fieldnames = [name.strip() for name in (reader.fieldnames or []) if name is not None]
            required = {"question_id", "engineer_question"}
            missing = required.difference(fieldnames)
            if missing:
                names = ", ".join(sorted(missing))
                raise ValueError(f"Input CSV must contain required column(s): {names}.")
            if len(fieldnames) != len(set(fieldnames)):
                raise ValueError("Input CSV contains duplicate column names.")
            for row_number, row in enumerate(reader, start=2):
                if None in row:
                    raise ValueError(f"Malformed input CSV row {row_number}: too many fields.")
                row_id = (row.get("question_id") or "").strip()
                question = (row.get("engineer_question") or "").strip()
                if not row_id:
                    raise ValueError(f"Evaluation CSV row {row_number} requires a non-blank question_id.")
                if not question:
                    raise ValueError(f"Evaluation CSV row {row_number} requires a non-blank engineer_question.")
                if row_id in seen:
                    raise ValueError(f"Duplicate evaluation id at CSV row {row_number}: {row_id}")
                expected = (row.get("expected_output") or "").strip() or None
                records.append(EvaluationRecord(
                    row_id,
                    question,
                    expected,
                    _normalize_expected_sources(row.get("expected_sources")),
                ))
                seen.add(row_id)
                if max_questions is not None and len(records) >= max_questions:
                    break
    except csv.Error as exc:
        raise ValueError(f"Could not parse input CSV {input_path}: {exc}") from exc
    if not records:
        raise ValueError(f"Evaluation dataset contains no questions: {input_path}")
    return records, digest.hexdigest()


def _retrieved_passages(response) -> list[RetrievedPassage]:
    return [
        RetrievedPassage(
            rank=index,
            passage_id=passage.passage_id,
            source_id=passage.source_id,
            text=passage.text,
        )
        for index, passage in enumerate(response.passages, start=1)
    ]

from deepeval.metrics.g_eval import GEvalTemplate
import textwrap
class CustomTemplate(GEvalTemplate):
    @staticmethod
    def generate_evaluation_steps(parameters: str, criteria: str):
        return textwrap.dedent(f"""
            Evaluate {parameters} based on: {criteria}.
            You must return strictly valid JSON. Do not include markdown codeblocks.
            Format: {{ "steps": [ "step 1", "step 2" ] }}
        """)

def evaluate_dataset_bulk(
    records: list[EvaluationRecord],
    retriever: LocalRetriever,
    runtime_config: EvaluationRuntimeModelConfig,
    portkey_api_key: str | None = None,
    output_path: str | Path | None = None,
    tracing_enabled: bool = False,
):
    from .tracing import evaluation_trace
    from deepeval.metrics import (
        ContextualRelevancyMetric,
        ContextualRecallMetric,
        ContextualPrecisionMetric,
        FaithfulnessMetric,
        AnswerRelevancyMetric,
    )
    from deepeval.metrics import GEval
    from deepeval.test_case import LLMTestCase, SingleTurnParams
    judge = build_judge(runtime_config, portkey_api_key)
    faithfulness_metric = FaithfulnessMetric(threshold=0.5,
                                              model=judge,
                                              async_mode=False,
                                              truths_extraction_limit=5)  
    answer_relevancy_metric = AnswerRelevancyMetric(threshold=0.5, model=judge)
    contextual_relevancy_metric = ContextualRelevancyMetric(
        threshold=0.5,
        model=judge,
        include_reason=True,
        async_mode=False,
    )
    contextual_recall_metric = ContextualRecallMetric(
            threshold=0.5,
            model=judge,
            include_reason=True,
            async_mode=False,
        )
    contextual_precision_metric = ContextualPrecisionMetric(
            threshold=0.5,
            model=judge,
            include_reason=True,
            async_mode=False,
        )

    test_cases = []
    for record in records:
      with evaluation_trace(tracing_enabled, f"evaluate:{record.id}"):
          response = answer_question(
              record.input,
              retriever,
              runtime_config.chat_model.model_name,
              runtime_config, portkey_api_key
          )
          test_case = build_test_case(record, response, response.passages)
          test_cases.append(test_case)
      
    from deepeval import evaluate
    from deepeval.evaluate import DisplayConfig, AsyncConfig, CacheConfig, ErrorConfig
    metrics = [contextual_relevancy_metric, 
                contextual_recall_metric,
                contextual_precision_metric,
                faithfulness_metric,
                answer_relevancy_metric]
    evaluate(test_cases, metrics, error_config=ErrorConfig(ignore_errors=True),
              display_config=DisplayConfig(results_folder=output_path, file_type="md"),
              async_config=AsyncConfig(run_async=False),
              cache_config=CacheConfig(use_cache=False, write_cache=False) )

def resolve_runtime_model_config(
    settings,
    model_source: str | None = None,
    chat_model: str | None = None,
    judge_model: str | None = None,
    portkey_url: str | None = None,
    portkey_provider: str | None = None,
    chunking_config=None,
) -> StartupValidationResult:
    source_value = (model_source or settings.model_source or "local").strip().lower()
    messages: list[str] = []
    if source_value not in {"local", "portkey"}:
        return StartupValidationResult("invalid", (f"Unsupported model source: {source_value}",))

    source_origin = "cli" if model_source else ("environment" if settings.model_source else "default")
    chat_name = (chat_model or settings.chat_model).strip()
    judge_name = (judge_model or settings.judge_model).strip()
    if not chat_name:
        messages.append("A chat model must be provided for evaluation runs.")
    if not judge_name:
        messages.append("A judge model must be provided for evaluation runs.")

    chunking_strategy = getattr(chunking_config, "strategy", "section")
    chunking_settings = chunking_config.as_dict() if chunking_config is not None else {}
    if chunking_strategy in {"fixed", "recursive"}:
        if chunking_config is None:
            messages.append("Chunking configuration is required for fixed and recursive strategies.")
        else:
            if chunking_config.chunk_size <= 0:
                messages.append("Chunk size must be positive.")
            if chunking_config.chunk_overlap < 0 or chunking_config.chunk_overlap >= chunking_config.chunk_size:
                messages.append("Chunk overlap must be non-negative and less than chunk size.")
    if chunking_strategy == "semantic":
        if chunking_config is None or not chunking_config.embedding_model:
            messages.append("Semantic chunking requires an embedding model.")

    portkey_config = None
    if source_value == "portkey":
        resolved_url = (portkey_url or settings.portkey_url or "").strip()
        if not resolved_url:
            messages.append("Portkey model source requires a gateway URL.")
        if not settings.portkey_api_key:
            messages.append("Portkey model source requires an API key in the environment.")
        portkey_config = PortkeyConnectionConfig(
            base_url=resolved_url,
            api_key_present=bool(settings.portkey_api_key),
            virtual_key_present=bool(settings.portkey_virtual_key),
            provider_context=(portkey_provider or settings.portkey_provider or None),
        )

    if messages:
        return StartupValidationResult("invalid", tuple(messages))

    runtime_config = EvaluationRuntimeModelConfig(
        model_source=EvaluationModelSource(
            source=source_value,
            is_default=not bool(model_source),
            selection_origin=source_origin,
        ),
        chat_model=EvaluationModelRoleConfig(
            role="chat",
            model_name=chat_name,
            source=source_value,
            provided_by="cli" if chat_model else "environment",
        ),
        judge_model=EvaluationModelRoleConfig(
            role="judge",
            model_name=judge_name,
            source="local" if source_value == "portkey" else source_value,
            provided_by="cli" if judge_model else ("environment" if settings.judge_model else "default"),
        ),
        chunking_strategy=chunking_strategy,
        chunking_settings=chunking_settings,
        portkey=portkey_config,
        validation_status="valid",
        validation_messages=(),
    )
    return StartupValidationResult("valid", (), runtime_config)


def write_evaluation_report(report: EvaluationReport, output_path: Path) -> None:
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text(json.dumps(asdict(report), indent=2), encoding="utf-8")


def format_evaluation_report(report: EvaluationReport) -> str:
    lines = [
        f"Evaluation {report.run_id} ({report.dataset.get('id', 'dataset')})",
        f"Questions: {report.counts['total']} total, {report.counts['evaluated']} evaluated, "
        f"{report.counts['failed']} failed",
    ]
    for name, aggregate in report.aggregates.items():
        score = "unavailable" if aggregate["score"] is None else f"{aggregate['score']:.3f}"
        lines.append(f"{name}: {score} ({aggregate['eligible']}/{aggregate['total']} eligible)")
    return "\n".join(lines)
