from pathlib import Path
import hashlib
import json
import re

from .ingestion import chunk_document, discover_documents
from .models import ChunkingConfig, DocumentPassage


class LocalRetriever:
    def __init__(self, db_dir: Path, embedding_model: str, top_k: int = 4):
        self.db_dir, self.embedding_model, self.top_k = db_dir, embedding_model, top_k
        self._store = None
        self._passages: list[DocumentPassage] = []
        self.index_identity = ""

    def build(
        self,
        data_dir: Path,
        strategy: str = "section",
        chunking: ChunkingConfig | None = None,
    ) -> tuple[int, int, list[str]]:
        documents, errors = discover_documents(data_dir)
        chunking = chunking or ChunkingConfig(strategy=strategy, embedding_model=self.embedding_model)
        embeddings = None
        if chunking.strategy == "semantic":
            try:
                from langchain_ollama import OllamaEmbeddings
                print(f"Using embedding model: {chunking.embedding_model or self.embedding_model}")
                embeddings = OllamaEmbeddings(
                    model=chunking.embedding_model or self.embedding_model,
                )
            except (ImportError, OSError, RuntimeError, ValueError) as exc:
                print(exc)
                raise RuntimeError("Semantic chunking requires an available local embedding model.") from exc
        self._passages = [
            passage for doc in documents
            for passage in chunk_document(doc, config=chunking, embeddings=embeddings)
        ]
        identity_payload = {
            "sources": [(doc.source_id, doc.content_hash) for doc in documents],
            "chunking": chunking.as_dict(),
            "embedding_model": self.embedding_model,
        }
        self.index_identity = hashlib.sha256(
            json.dumps(identity_payload, sort_keys=True).encode("utf-8")
        ).hexdigest()
        self.db_dir.mkdir(parents=True, exist_ok=True)
        try:
            from langchain_chroma import Chroma
            from langchain_ollama import OllamaEmbeddings
            print(f"Using embedding model: {chunking.embedding_model or self.embedding_model}")
            embeddings = OllamaEmbeddings(
                model=chunking.embedding_model or self.embedding_model,
            )
            self._store = Chroma(collection_name="l1_assistant", persist_directory=str(self.db_dir),
                                 embedding_function=embeddings)
            self._store.reset_collection()
            self._store.add_texts([p.text for p in self._passages],
                                  metadatas=[{"source": p.source_id, "heading": p.heading,
                                              "passage_id": p.passage_id,
                                              "chunking_strategy": p.chunking_strategy,
                                              "source_hash": p.source_hash} for p in self._passages],
                                  ids=[p.passage_id for p in self._passages])
        except (ImportError, OSError, RuntimeError, ValueError) as exc:
            if chunking.strategy == "semantic":
                print(exc)
                raise RuntimeError("Semantic chunking requires a working local vector index.") from exc
            self._store = None
        return len(documents), len(self._passages), errors

    def search(self, question: str) -> list[DocumentPassage]:
        if self._store is not None:
            docs = self._store.similarity_search(question, k=self.top_k)
            by_text = {p.text: p for p in self._passages}
            return [by_text[d.page_content] for d in docs if d.page_content in by_text]
        terms = set(re.findall(r"\w+", question.lower()))
        ranked = sorted(self._passages,
                        key=lambda p: len(terms & set(re.findall(r"\w+", p.text.lower()))),
                        reverse=True)
        return ranked[:self.top_k]

    def search_with_scores(self, question: str) -> list[tuple[DocumentPassage, float | None]]:
        """Return ranked passages with optional backend scores for evaluation."""
        if self._store is not None:
            docs = self._store.similarity_search_with_score(question, k=self.top_k)
            by_text = {p.text: p for p in self._passages}
            return [
                (by_text[d.page_content], float(score))
                for d, score in docs
                if d.page_content in by_text
            ]
        passages = self.search(question)
        return [(passage, None) for passage in passages]
