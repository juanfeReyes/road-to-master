from typing import Any

from .models import ChunkingConfig


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
