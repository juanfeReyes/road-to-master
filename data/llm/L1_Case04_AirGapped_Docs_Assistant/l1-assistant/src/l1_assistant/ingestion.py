from hashlib import sha256
from pathlib import Path
import re

from .models import DocumentPassage, SourceDocument


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


def chunk_document(document: SourceDocument, strategy: str = "section",
                   chunk_size: int = 800) -> list[DocumentPassage]:
    if strategy == "fixed":
        parts = [document.content[i:i + chunk_size] for i in range(0, len(document.content), chunk_size)]
        return [DocumentPassage(f"{document.source_id}:{i}", document.source_id, "", text.strip(), i)
                for i, text in enumerate(parts) if text.strip()]
    sections = re.split(r"(?m)^(#{1,6}\s+.+)$", document.content)
    heading, passages, position = "", [], 0
    for part in sections:
        if re.match(r"^#{1,6}\s+", part):
            heading = part.strip()
        elif part.strip():
            text = part.strip()
            passages.append(DocumentPassage(f"{document.source_id}:{position}", document.source_id,
                                             heading, text, position))
            position += 1
    return passages

