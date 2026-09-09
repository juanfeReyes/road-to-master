import argparse
import sys
from pathlib import Path

from .answering import answer_question
from .chunking import config_from_args
from .config import Settings
from .pipeline import (
    default_report_path,
    evaluate_dataset,
    evaluate_questions,
    format_evaluation_report,
    format_report,
    load_evaluation_dataset,
    load_questions,
    resolve_runtime_model_config,
    write_evaluation_report,
    write_report,
)
from .models import EvaluationReport
from .tracing import configure_offline
from datetime import datetime, timezone
from uuid import uuid4
from .retrieval import LocalRetriever


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(prog="l1-assistant")
    sub = parser.add_subparsers(dest="command", required=True)
    for name in ("index", "ask", "pipeline", "evaluate"):
        command = sub.add_parser(name)
        command.add_argument("--data-dir", type=Path, default=None)
        command.add_argument("--db-dir", type=Path, default=None)
        if name == "index":
            command.add_argument(
                "--chunking-strategy", "--chunk-strategy",
                dest="chunking_strategy",
                choices=("section", "fixed", "recursive", "semantic"),
                default="section",
            )
            command.add_argument("--chunk-size", type=int, default=800)
            command.add_argument("--chunk-overlap", type=int, default=100)
            command.add_argument("--separators", default=None)
            command.add_argument("--embedding-model", default=None)
            command.add_argument("--breakpoint-threshold-type", default="percentile")
            command.add_argument("--breakpoint-threshold-amount", type=float, default=95.0)
        elif name == "ask":
            command.add_argument("question")
        elif name == "pipeline":
            command.add_argument("--questions", type=Path, default=Path("../data/engineer_questions.csv"))
            command.add_argument("--output", type=Path, default=None)
        else:
            command.add_argument("--dataset", type=Path, default=None)
            command.add_argument("--output", type=Path, required=True)
            command.add_argument("--metrics", default="generator,retrieval")
            command.add_argument("--model-source", choices=("local", "portkey"), default=None)
            command.add_argument("--chat-model", default=None)
            command.add_argument("--judge-model", default=None)
            command.add_argument("--portkey-url", default=None)
            command.add_argument("--portkey-provider", default=None)
            command.add_argument("--max-questions", type=int, default=None)
            command.add_argument("--trace", action="store_true")
            command.add_argument("--threshold", action="append", default=[])
            command.add_argument(
                "--chunking-strategy",
                choices=("section", "fixed", "recursive", "semantic"),
                default="section",
            )
            command.add_argument("--chunk-size", type=int, default=800)
            command.add_argument("--chunk-overlap", type=int, default=100)
            command.add_argument("--separators", default=None)
            command.add_argument("--embedding-model", default=None)
            command.add_argument("--breakpoint-threshold-type", default="percentile")
            command.add_argument("--breakpoint-threshold-amount", type=float, default=95.0)
    return parser


def main(argv: list[str] | None = None) -> int:
    args = build_parser().parse_args(argv)
    try:
        settings = Settings.from_values(args.data_dir, args.db_dir)
        settings.validate_data_dir()
        embedding_model = getattr(args, "embedding_model", None) or settings.embedding_model
        retriever = LocalRetriever(settings.db_dir, embedding_model, settings.top_k)
        chunking = config_from_args(
            strategy=getattr(args, "chunking_strategy", "section"),
            chunk_size=getattr(args, "chunk_size", 800),
            chunk_overlap=getattr(args, "chunk_overlap", 100),
            separators=getattr(args, "separators", None),
            breakpoint_threshold_type=getattr(args, "breakpoint_threshold_type", "percentile"),
            breakpoint_threshold_amount=getattr(args, "breakpoint_threshold_amount", 95.0),
            embedding_model=embedding_model,
        )
        if args.command == "index":
            files, passages, errors = retriever.build(settings.data_dir, chunking=chunking)
            print(f"Indexed files: {files}\nIndexed passages: {passages}")
            for error in errors:
                print(f"Warning: {error}", file=sys.stderr)
            return 0
        if args.command == "pipeline":
            if not settings.db_dir.exists():
                raise FileNotFoundError(f"Index does not exist: {settings.db_dir}; run index first.")
            retriever.build(settings.data_dir, chunking=chunking)
            questions = load_questions(args.questions)
            results = evaluate_questions(questions, retriever, settings.chat_model)
            output_path = args.output or default_report_path()
            write_report(results, output_path)
            print(format_report(results))
            print(f"\nReport saved to: {output_path}")
            return 0
        if args.command == "evaluate":
            if args.max_questions is not None and args.max_questions <= 0:
                raise ValueError("--max-questions must be positive.")
            configure_offline()
            dataset_path = args.dataset or (settings.data_dir / "engineer_questions.csv")
            records, dataset_hash = load_evaluation_dataset(dataset_path, args.max_questions)
            if not settings.db_dir.exists():
                raise FileNotFoundError(f"Index does not exist: {settings.db_dir}; run index first.")
            retriever.build(settings.data_dir, chunking=chunking)
            groups = tuple(group.strip() for group in args.metrics.split(",") if group.strip())
            if not set(groups).issubset({"generator", "retrieval"}) or not groups:
                raise ValueError("--metrics must contain generator, retrieval, or both.")
            thresholds = {}
            for item in args.threshold:
                try:
                    name, value = item.split("=", 1)
                    thresholds[name] = float(value)
                except ValueError as exc:
                    raise ValueError("Thresholds must use NAME=VALUE format.") from exc
            runtime_validation = resolve_runtime_model_config(
                settings,
                args.model_source,
                args.chat_model,
                args.judge_model,
                args.portkey_url,
                args.portkey_provider,
                chunking,
            )
            if runtime_validation.status != "valid" or runtime_validation.resolved_config is None:
                raise ValueError(" ".join(runtime_validation.messages))
            runtime_config = runtime_validation.resolved_config
            print(
                f"Model source: {runtime_config.model_source.source}\n"
                f"Chat model: {runtime_config.chat_model.model_name}\n"
                f"Judge model: {runtime_config.judge_model.model_name}\n"
                f"Chunking strategy: {runtime_config.chunking_strategy}"
            )
            if runtime_config.portkey is not None:
                print(f"Portkey URL: {runtime_config.portkey.base_url}")
            definitions, results, aggregates = evaluate_dataset(
                records,
                retriever,
                runtime_config,
                settings.portkey_api_key,
                groups,
                thresholds,
                args.trace,
            )
            now = datetime.now(timezone.utc).isoformat()
            counts = {
                "total": len(results),
                "evaluated": sum(result.status == "evaluated" for result in results),
                "partial": sum(result.status == "partial" for result in results),
                "failed": sum(result.status == "failed" for result in results),
                "skipped": sum(result.status == "skipped" for result in results),
            }
            report = EvaluationReport(
                schema_version="1.0",
                run_id=uuid4().hex,
                started_at=now,
                finished_at=datetime.now(timezone.utc).isoformat(),
                dataset={
                    "id": dataset_path.stem,
                    "version": "1",
                    "format": dataset_path.suffix.lower().lstrip("."),
                    "path": str(dataset_path),
                    "hash": dataset_hash,
                    "row_count": len(records),
                },
                configuration={
                    "metric_groups": list(groups),
                    "judge_model": runtime_config.judge_model.model_name,
                    "generator_model": runtime_config.chat_model.model_name,
                    "top_k": settings.top_k,
                    "tracing_enabled": args.trace,
                    "chunking_strategy": chunking.strategy,
                    "chunking": chunking.as_dict(),
                    "embedding_model": embedding_model,
                    **runtime_config.as_report_dict(),
                },
                metric_definitions=[{
                    "name": definition.name,
                    "group": definition.group,
                    "threshold": definition.threshold,
                    "scale": "0.0-1.0",
                    "requires_reference": definition.requires_reference,
                } for definition in definitions],
                aggregates=aggregates,
                counts=counts,
                results=results,
                traces={"enabled": args.trace} if args.trace else None,
            )
            write_evaluation_report(report, args.output)
            print(format_evaluation_report(report))
            print(f"Report saved to: {args.output}")
            return 1 if counts["failed"] or counts["partial"] else 0
        if not settings.db_dir.exists():
            raise FileNotFoundError(f"Index does not exist: {settings.db_dir}; run index first.")
        retriever.build(settings.data_dir)
        response = answer_question(args.question, retriever, settings.chat_model)
        print(f"Answer: {response.answer}")
        print(f"Sources: {', '.join(response.sources) if response.sources else 'none'}")
        print(f"Score: {response.score:.2f}/1.00" if response.score is not None else "Score: unavailable")
        print(f"Score explanation: {response.score_reason}")
        return 0
    except (OSError, ValueError, RuntimeError) as exc:
        print(f"Error: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
