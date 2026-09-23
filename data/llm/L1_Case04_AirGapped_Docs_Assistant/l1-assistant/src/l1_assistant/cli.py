import argparse
import sys
from pathlib import Path

from .answering import answer_question
from .chunking import config_from_args
from .config import Settings
from .pipeline import (
    evaluate_dataset_bulk,
    load_evaluation_dataset,
    process_data_set,
    resolve_runtime_model_config,
    save_answers,
)
from .tracing import configure_offline
from .retrieval import LocalRetriever


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(prog="l1-assistant")
    sub = parser.add_subparsers(dest="command", required=True)
    for name in ("index", "ask", "pipeline", "evaluate"):
        command = sub.add_parser(name)
        command.add_argument("--data-dir", type=Path, default="..\\data")
        command.add_argument("--db-dir", type=Path, default=".\\var\\chroma")
        command.add_argument("--questions", type=Path, default=Path("../data/engineer_questions.csv"))
        command.add_argument("--dataset", type=Path, default=None)
        command.add_argument("--output", type=Path, required=True)
        command.add_argument("--metrics", default="generator,retrieval")
        command.add_argument("--model-source", choices=("local", "portkey"), default="local")
        command.add_argument("--chat-model", default="llama3.2:3b")
        command.add_argument("--judge-model", default="gemma:7b")
        command.add_argument("--portkey-url", default=None)
        command.add_argument("--portkey-provider", default=None)
        command.add_argument("--max-questions", type=int, default=None)
        command.add_argument("--trace", action="store_true")
        command.add_argument("--threshold", action="append", default=[])
        command.add_argument(
            "--chunking-strategy",
            choices=("section", "fixed", "recursive", "semantic"),
            default="recursive",
        )
        command.add_argument("--chunk-size", type=int, default=500)
        command.add_argument("--chunk-overlap", type=int, default=100)
        command.add_argument("--separators", default="`n`n|`n| |")
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
    
        if args.max_questions is not None and args.max_questions <= 0:
            raise ValueError("--max-questions must be positive.")
        configure_offline()
        dataset_path = args.questions or (settings.data_dir / "engineer_questions.csv")
        print(f"Reading questions from: {dataset_path}")
        records, dataset_hash = load_evaluation_dataset(dataset_path, args.max_questions)
        # 1. Index documents
        retriever.build(settings.data_dir, getattr(args, "chunking_strategy", "section"), chunking=chunking)
        
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
        responses = process_data_set(records, retriever, runtime_config, settings, args)

        if args.command == "evaluate":
            evaluate_dataset_bulk(
                responses,
                runtime_config,
                settings,
                args,
            )

        # Write report
        save_answers(responses, args)
        print(f"Report saved to: {args.output}")
        return 0
    except (OSError, ValueError, RuntimeError) as exc:
        print(f"Error: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
