from hashlib import sha256
from pathlib import Path
import re

from .chunking import build_splitter
from .models import ChunkingConfig, DocumentPassage, SourceDocument


def discover_documents(data_dir: Path) -> tuple[list[SourceDocument], list[str]]:
    if not data_dir.is_dir():
        raise FileNotFoundError(f"Data directory does not exist: {data_dir}")
    documents, errors = [], []
    for path in sorted(data_dir.rglob("*.md")):
        try:
            content = path.read_text(encoding="utf-8")
        except (OSError, UnicodeError) as exc:
            errors.append(f"{path}: {exc}")
            continue
        relative = path.relative_to(data_dir).as_posix()
        documents.append(SourceDocument(relative, path, content, sha256(content.encode()).hexdigest(),
                                        path.stat().st_mtime))
    return documents, errors


def chunk_document(
    document: SourceDocument,
    strategy: str = "section",
    chunk_size: int = 800,
    config: ChunkingConfig | None = None,
    embeddings=None,
) -> list[DocumentPassage]:
    config = config or ChunkingConfig(strategy=strategy, chunk_size=chunk_size)
    if config.strategy in {"fixed", "recursive", "semantic"}:
        splitter = build_splitter(config, embeddings)
        chunks = splitter.create_documents(
            [document.content],
            metadatas=[{"source_id": document.source_id, "source_hash": document.content_hash}],
        )
        return [
            DocumentPassage(
                f"{document.source_id}:{index}",
                document.source_id,
                "",
                chunk.page_content.strip(),
                index,
                config.strategy,
                document.content_hash,
            )
            for index, chunk in enumerate(chunks)
            if chunk.page_content.strip()
        ]
    sections = re.split(r"(?m)^(#{1,6}\s+.+)$", document.content)
    heading, passages, position = "", [], 0
    for part in sections:
        if re.match(r"^#{1,6}\s+", part):
            heading = part.strip()
        elif part.strip():
            text = part.strip()
            passages.append(DocumentPassage(f"{document.source_id}:{position}", document.source_id,
                                             heading, text, position, "section", document.content_hash))
            position += 1
    return passages
