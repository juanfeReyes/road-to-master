"""Measure retrieval hit rates for section-aware and fixed-size chunking.

The evaluator should provide a reviewed expected-source mapping for the question CSV.
This script intentionally does not invent ground truth.
"""
import argparse
import csv
from pathlib import Path

from l1_assistant.config import Settings
from l1_assistant.retrieval import LocalRetriever


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("questions", type=Path)
    parser.add_argument("--data-dir", type=Path, required=True)
    parser.add_argument("--db-dir", type=Path, required=True)
    args = parser.parse_args()
    questions = list(csv.DictReader(args.questions.open(encoding="utf-8", newline="")))
    for strategy in ("section", "fixed"):
        retriever = LocalRetriever(args.db_dir / strategy, Settings.from_values().embedding_model)
        files, passages, _ = retriever.build(args.data_dir, strategy)
        print(f"{strategy}: indexed_files={files}, passages={passages}")
        for row in questions:
            text = row.get("question") or row.get("text") or ""
            sources = sorted({p.source_id for p in retriever.search(text)})
            print(f"{row.get('id', '?')}: {', '.join(sources) or 'none'}")


if __name__ == "__main__":
    main()
