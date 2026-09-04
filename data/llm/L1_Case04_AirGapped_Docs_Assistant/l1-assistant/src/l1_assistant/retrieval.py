from pathlib import Path
import re

from .ingestion import chunk_document, discover_documents
from .models import DocumentPassage


class LocalRetriever:
    def __init__(self, db_dir: Path, embedding_model: str, top_k: int = 4):
        self.db_dir, self.embedding_model, self.top_k = db_dir, embedding_model, top_k
        self._store = None
        self._passages: list[DocumentPassage] = []

    def build(self, data_dir: Path, strategy: str = "section") -> tuple[int, int, list[str]]:
        documents, errors = discover_documents(data_dir)
        self._passages = [passage for doc in documents for passage in chunk_document(doc, strategy)]
        self.db_dir.mkdir(parents=True, exist_ok=True)
        try:
            from langchain_chroma import Chroma
            from langchain_huggingface import HuggingFaceEmbeddings
            embeddings = HuggingFaceEmbeddings(model_name=self.embedding_model,
                                               model_kwargs={"local_files_only": True})
            self._store = Chroma(collection_name="l1_assistant", persist_directory=str(self.db_dir),
                                 embedding_function=embeddings)
            self._store.reset_collection()
            self._store.add_texts([p.text for p in self._passages],
                                  metadatas=[{"source": p.source_id, "heading": p.heading,
                                              "passage_id": p.passage_id} for p in self._passages],
                                  ids=[p.passage_id for p in self._passages])
        except (ImportError, OSError, RuntimeError, ValueError):
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
