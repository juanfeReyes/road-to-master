from pathlib import Path

from l1_assistant.ingestion import chunk_document, discover_documents


def test_discovers_markdown_and_chunks_with_source(tmp_path: Path):
    source = tmp_path / "manual.md"
    source.write_text("# Safety\nDisconnect power.\n\n# Steps\nReplace liner.", encoding="utf-8")
    documents, errors = discover_documents(tmp_path)
    passages = chunk_document(documents[0])
    assert not errors
    assert documents[0].source_id == "manual.md"
    assert any("Disconnect power" in passage.text for passage in passages)
    assert all(passage.source_id == "manual.md" for passage in passages)
