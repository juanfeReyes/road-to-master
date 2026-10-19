from hashlib import sha256
from pathlib import Path
import re

from .models import ChunkingConfig, DocumentPassage, SourceDocument
from langchain_core.documents import Document


def discover_documents(data_dir: Path) -> tuple[list[Document], list[str]]:
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
        documents.append(Document(page_content=content, metadata={"source": relative, "source_hash": sha256(content.encode("utf-8")).hexdigest()}))
    return documents, errors


