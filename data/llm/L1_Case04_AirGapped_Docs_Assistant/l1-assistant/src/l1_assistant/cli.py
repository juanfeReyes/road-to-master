import argparse
import sys
from pathlib import Path

from .answering import answer_question
from .config import Settings
from .retrieval import LocalRetriever


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(prog="l1-assistant")
    sub = parser.add_subparsers(dest="command", required=True)
    for name in ("index", "ask"):
        command = sub.add_parser(name)
        command.add_argument("--data-dir", type=Path, default=None)
        command.add_argument("--db-dir", type=Path, default=None)
        if name == "index":
            command.add_argument("--chunk-strategy", choices=("section", "fixed"), default="section")
        else:
            command.add_argument("question")
    return parser


def main(argv: list[str] | None = None) -> int:
    args = build_parser().parse_args(argv)
    try:
        settings = Settings.from_values(args.data_dir, args.db_dir)
        settings.validate_data_dir()
        retriever = LocalRetriever(settings.db_dir, settings.embedding_model, settings.top_k)
        if args.command == "index":
            files, passages, errors = retriever.build(settings.data_dir, args.chunk_strategy)
            print(f"Indexed files: {files}\nIndexed passages: {passages}")
            for error in errors:
                print(f"Warning: {error}", file=sys.stderr)
            return 0
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
