import asyncio
from dataclasses import asdict
from typing import Any, Iterable

from .models import (
    EvaluationRuntimeModelConfig,
    EvaluationRecord,
    MetricDefinition,
    MetricResult,
    QuestionEvaluation,
    RetrievedPassage,
)

try:
    from deepeval.metrics import (
        AnswerRelevancyMetric,
        ContextualPrecisionMetric,
        ContextualRecallMetric,
        ContextualRelevancyMetric,
        FaithfulnessMetric,
        GEval,
    )
    from deepeval.models import DeepEvalBaseLLM
    from deepeval.test_case import LLMTestCase, SingleTurnParams
except ImportError:  # pragma: no cover
    AnswerRelevancyMetric = ContextualPrecisionMetric = ContextualRecallMetric = None
    ContextualRelevancyMetric = FaithfulnessMetric = GEval = None
    DeepEvalBaseLLM = object
    LLMTestCase = None
    SingleTurnParams = None


class LocalJudge(DeepEvalBaseLLM):
    """DeepEval judge backed by the configured local LangChain model."""

    def __init__(self, model_name: str):
        self.model_name = model_name
        self._model = None

    def load_model(self):
        if self._model is None:
            try:
                from langchain_ollama import OllamaLLM
                self._model = OllamaLLM(model=self.model_name)
            except (ImportError, RuntimeError, OSError) as exc:
                raise RuntimeError(
                    f"Local judge model is unavailable: {self.model_name}"
                ) from exc
        return self._model

    def generate(self, prompt: str, schema: Any = None) -> str:
        return str(self.load_model().invoke(prompt))

    async def a_generate(self, prompt: str, schema: Any = None) -> str:
        model = self.load_model()
        if hasattr(model, "ainvoke"):
            return str(await model.ainvoke(prompt))
        return await asyncio.to_thread(self.generate, prompt, schema)

    def get_model_name(self) -> str:
        return f"local:{self.model_name}"


class PortkeyJudge(DeepEvalBaseLLM):
    """DeepEval judge backed by a Portkey-routed LangChain chat model."""

    def __init__(self, model_name: str, api_key: str, base_url: str, provider: str | None = None):
        self.model_name = model_name
        self.api_key = api_key
        self.base_url = base_url
        self.provider = provider
        self._model = None

    def load_model(self):
        if self._model is None:
            try:
                from langchain_openai import ChatOpenAI
                from portkey_ai import createHeaders
                portkey_headers = createHeaders(api_key=self.api_key, provider="azure-openai-eus2")
                self._model = ChatOpenAI(model=self.model_name, api_key=self.api_key, base_url=self.base_url, default_headers=portkey_headers)
            except (ImportError, RuntimeError, OSError) as exc:
                raise RuntimeError(
                    f"Portkey judge model is unavailable: {self.model_name}"
                ) from exc
        return self._model

    def generate(self, prompt: str, schema: Any = None) -> str:
        return str(self.load_model().invoke(prompt).content)

    async def a_generate(self, prompt: str, schema: Any = None) -> str:
        model = self.load_model()
        if hasattr(model, "ainvoke"):
            response = await model.ainvoke(prompt)
            return str(response.content)
        return await asyncio.to_thread(self.generate, prompt, schema)

    def get_model_name(self) -> str:
        return f"portkey:{self.model_name}"


def metric_definitions(groups: Iterable[str], thresholds: dict[str, float] | None = None):
    thresholds = thresholds or {}
    definitions: list[MetricDefinition] = []
    if "generator" in groups:
        definitions.extend([
            MetricDefinition("answer_relevancy", "generator", thresholds.get("answer_relevancy", 0.7)),
            MetricDefinition("faithfulness", "generator", thresholds.get("faithfulness", 0.7)),
            MetricDefinition("answer_correctness", "generator", thresholds.get("answer_correctness", 0.7), True),
        ])
    if "retrieval" in groups:
        definitions.extend([
            MetricDefinition("contextual_relevancy", "retrieval", thresholds.get("contextual_relevancy", 0.7)),
            MetricDefinition("contextual_precision", "retrieval", thresholds.get("contextual_precision", 0.7), True),
            MetricDefinition("contextual_recall", "retrieval", thresholds.get("contextual_recall", 0.7), True),
        ])
    return definitions


def build_test_case(record: EvaluationRecord, actual_output: str,
                    retrieval_context: list[RetrievedPassage]):
    if LLMTestCase is None:
        raise RuntimeError("DeepEval is not installed.")
    values = {
        "input": record.input,
        "actual_output": actual_output,
        "retrieval_context": [passage.text for passage in retrieval_context],
    }
    if record.expected_output is not None:
        values["expected_output"] = record.expected_output
    return LLMTestCase(**values)


def _unavailable(reason: str) -> MetricResult:
    return MetricResult(reason=reason, available=False)


def _metric_result(metric: Any, threshold: float) -> MetricResult:
    score = getattr(metric, "score", None)
    if score is None:
        return _unavailable(getattr(metric, "reason", "") or "Metric returned no score.")
    score = float(score)
    return MetricResult(score=score, passed=score >= threshold,
                        reason=getattr(metric, "reason", "") or "", available=True)


def deterministic_checks(record: EvaluationRecord, actual_output: str,
                         retrieval_context: list[RetrievedPassage]):
    checks = {
        "non_empty_answer": MetricResult(
            score=1.0 if actual_output.strip() else 0.0,
            passed=bool(actual_output.strip()),
            reason="Answer is present." if actual_output.strip() else "Answer is empty.",
            available=True,
        )
    }
    if record.expected_sources:
        actual_sources = {passage.source_id for passage in retrieval_context}
        hits = actual_sources.intersection(record.expected_sources)
        checks["expected_source_hit"] = MetricResult(
            score=1.0 if hits else 0.0,
            passed=bool(hits),
            reason=f"Matched sources: {', '.join(sorted(hits))}" if hits
            else "No expected source retrieved.",
            available=True,
        )
    else:
        checks["expected_source_hit"] = _unavailable("No expected_sources supplied.")
    return checks


def _make_metric(judge: LocalJudge, definition: MetricDefinition):
    if definition.name == "answer_relevancy":
        return AnswerRelevancyMetric(model=judge, threshold=definition.threshold)
    if definition.name == "faithfulness":
        return FaithfulnessMetric(model=judge, threshold=definition.threshold)
    if definition.name == "answer_correctness":
        return GEval(
            name="Answer Correctness",
            evaluation_params=[
                SingleTurnParams.INPUT,
                SingleTurnParams.ACTUAL_OUTPUT,
                SingleTurnParams.EXPECTED_OUTPUT,
            ],
            criteria="The actual answer is factually correct, complete, and directly answers the input.",
            model=judge,
            threshold=definition.threshold,
        )
    if definition.name == "contextual_relevancy":
        return ContextualRelevancyMetric(model=judge, threshold=definition.threshold)
    if definition.name == "contextual_precision":
        return ContextualPrecisionMetric(model=judge, threshold=definition.threshold)
    if definition.name == "contextual_recall":
        return ContextualRecallMetric(model=judge, threshold=definition.threshold)
    raise ValueError(f"Unsupported metric: {definition.name}")


def evaluate_case(record: EvaluationRecord, actual_output: str,
                  retrieval_context: list[RetrievedPassage], judge: LocalJudge,
                  definitions: list[MetricDefinition]) -> QuestionEvaluation:
    result = QuestionEvaluation(
        id=record.id,
        input=record.input,
        actual_output=actual_output,
        retrieval_context=retrieval_context,
        deterministic_checks=deterministic_checks(record, actual_output, retrieval_context),
    )
    if not actual_output.strip():
        result.status = "partial"
    try:
        test_case = build_test_case(record, actual_output, retrieval_context)
        for definition in definitions:
            target = result.generator_metrics if definition.group == "generator" else result.retrieval_metrics
            if definition.requires_reference and not record.expected_output:
                target[definition.name] = _unavailable("Expected output is required.")
                continue
            metric = _make_metric(judge, definition)
            metric.measure(test_case, _show_indicator=False)
            target[definition.name] = _metric_result(metric, definition.threshold)
    except (OSError, RuntimeError, ValueError, ConnectionError) as exc:
        result.status = "failed"
        result.error = str(exc)
    return result


def aggregate_results(results: list[QuestionEvaluation], definitions: list[MetricDefinition]):
    aggregates = {}
    for definition in definitions:
        scores = []
        for result in results:
            metrics = result.generator_metrics if definition.group == "generator" else result.retrieval_metrics
            value = metrics.get(definition.name)
            if value and value.available and value.score is not None:
                scores.append(value.score)
        aggregates[definition.name] = {
            "score": sum(scores) / len(scores) if scores else None,
            "eligible": len(scores),
            "total": len(results),
        }
    return aggregates


def build_judge(runtime_config: EvaluationRuntimeModelConfig, portkey_api_key: str | None = None):
    if runtime_config.model_source.source == "portkey":
        if runtime_config.portkey is None or not portkey_api_key:
            raise RuntimeError("Portkey judge configuration is incomplete.")
        return PortkeyJudge(
            runtime_config.judge_model.model_name,
            portkey_api_key,
            runtime_config.portkey.base_url,
            runtime_config.portkey.provider_context,
        )
    return LocalJudge(runtime_config.judge_model.model_name)


def serialize_metric(value):
    return asdict(value)
