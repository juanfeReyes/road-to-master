from pathlib import Path

from l1_assistant.models import ChunkingConfig
from l1_assistant.retrieval import LocalRetriever


def test_build_records_strategy_aware_passages_and_identity(tmp_path: Path):
    data_dir = tmp_path / "data"
    data_dir.mkdir()
    (data_dir / "guide.md").write_text("# Heading\nA short passage.", encoding="utf-8")

    retriever = LocalRetriever(tmp_path / "db", "missing-local-model")
    files, passages, errors = retriever.build(
        data_dir,
        chunking=ChunkingConfig(strategy="fixed", chunk_size=10, chunk_overlap=0),
    )

    assert files == 1
    assert passages >= 1
    assert not errors
    assert retriever.index_identity
    assert all(passage.chunking_strategy == "fixed" for passage in retriever._passages)
