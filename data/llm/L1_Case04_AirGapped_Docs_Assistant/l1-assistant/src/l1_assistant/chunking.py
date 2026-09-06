from typing import Any

from .models import ChunkingConfig


def build_splitter(config: ChunkingConfig, embeddings: Any = None):
    if config.strategy == "section":
        return None
    try:
        if config.strategy == "fixed":
            from langchain_text_splitters import CharacterTextSplitter
            return CharacterTextSplitter(
                separator="",
                chunk_size=config.chunk_size,
                chunk_overlap=config.chunk_overlap,
                strip_whitespace=True,
            )
        if config.strategy == "recursive":
            from langchain_text_splitters import RecursiveCharacterTextSplitter
            return RecursiveCharacterTextSplitter(
                separators=list(config.separators),
                chunk_size=config.chunk_size,
                chunk_overlap=config.chunk_overlap,
                strip_whitespace=True,
            )
        if embeddings is None:
            raise RuntimeError("Semantic chunking requires a local embedding model.")
        from langchain_experimental.text_splitter import SemanticChunker
        return SemanticChunker(
            embeddings,
            breakpoint_threshold_type=config.breakpoint_threshold_type,
            breakpoint_threshold_amount=config.breakpoint_threshold_amount,
        )
    except ImportError as exc:
        raise RuntimeError(
            f"Chunking strategy '{config.strategy}' requires an installed LangChain splitter dependency."
        ) from exc


def config_from_args(
    strategy: str = "section",
    chunk_size: int = 800,
    chunk_overlap: int = 100,
    separators: str | None = None,
    breakpoint_threshold_type: str = "percentile",
    breakpoint_threshold_amount: float = 95.0,
    embedding_model: str | None = None,
) -> ChunkingConfig:
    separator_values = tuple(separators.split("|")) if separators else ("\n\n", "\n", " ", "")
    return ChunkingConfig(
        strategy=strategy,
        chunk_size=chunk_size,
        chunk_overlap=chunk_overlap,
        separators=separator_values,
        breakpoint_threshold_type=breakpoint_threshold_type,
        breakpoint_threshold_amount=breakpoint_threshold_amount,
        embedding_model=embedding_model,
    )
