from pathlib import Path

import pytest

from l1_assistant.chunking import config_from_args
from l1_assistant.ingestion import chunk_document
from l1_assistant.models import ChunkingConfig, SourceDocument


def source(text: str) -> SourceDocument:
    return SourceDocument("doc.md", Path("doc.md"), text, "hash", 0.0)


def test_fixed_chunking_uses_langchain_splitter():
    passages = chunk_document(source("abcdefghij"), config=ChunkingConfig(
        strategy="fixed", chunk_size=4, chunk_overlap=0,
    ))
    assert [passage.text for passage in passages] == ["abcd", "efgh", "ij"]
    assert all(passage.chunking_strategy == "fixed" for passage in passages)


def test_recursive_chunking_preserves_paragraph_boundaries():
    passages = chunk_document(source("one two\n\nthree four"), config=ChunkingConfig(
        strategy="recursive", chunk_size=12, chunk_overlap=0,
    ))
    assert len(passages) == 2
    assert passages[0].text == "one two"


def test_config_rejects_invalid_overlap():
    with pytest.raises(ValueError, match="chunk_overlap"):
        ChunkingConfig(strategy="fixed", chunk_size=4, chunk_overlap=4)


def test_config_from_args_parses_separator_list():
    config = config_from_args(strategy="recursive", separators="A|B|")
    assert config.separators == ("A", "B", "")
