from dataclasses import dataclass
from pathlib import Path
import os


@dataclass(frozen=True)
class Settings:
    data_dir: Path
    db_dir: Path
    embedding_model: str
    chat_model: str
    top_k: int = 4
    min_relevance: float = 0.25

    @classmethod
    def from_values(cls, data_dir: Path | None = None, db_dir: Path | None = None) -> "Settings":
        return cls(
            data_dir=(data_dir or Path(os.getenv("L1_DATA_DIR", "../data"))).resolve(),
            db_dir=(db_dir or Path(os.getenv("L1_DB_DIR", "var/chroma"))).resolve(),
            embedding_model=os.getenv("L1_EMBEDDING_MODEL", "sentence-transformers/all-MiniLM-L6-v2"),
            chat_model=os.getenv("L1_CHAT_MODEL", "llama3.2"),
        )

    def validate_data_dir(self) -> None:
        if not self.data_dir.is_dir():
            raise FileNotFoundError(f"Data directory does not exist: {self.data_dir}")
