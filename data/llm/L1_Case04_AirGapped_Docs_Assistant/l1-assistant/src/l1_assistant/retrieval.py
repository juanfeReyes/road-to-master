from pathlib import Path
import hashlib
import json
import re
import uuid
from langchain_core.documents import Document
from langchain_core.tools import StructuredTool
from langchain_graph_retriever.transformers import ShreddingTransformer

from langchain_text_splitters import CharacterTextSplitter, RecursiveCharacterTextSplitter

from .ingestion import chunk_document, discover_documents
from .models import ChunkingConfig, DocumentPassage

from deepagents.backends import StateBackend
from langchain.tools import tool

from graph_retriever.strategies import Eager
from langchain_graph_retriever import GraphRetriever
from langchain_chroma.vectorstores import Chroma


class LocalRetriever:
    def __init__(self, db_dir: Path, embedding_model: str, top_k: int = 4):
        self.db_dir, self.embedding_model, self.top_k = db_dir, embedding_model, top_k
        self._store = None
        self._passages: list[DocumentPassage] = []
        self.index_identity = ""
        self._backend = StateBackend()

    def build(
            self,
            data_dir: Path,
            strategy: str = "section",
            chunking: ChunkingConfig | None = None,
        ) -> tuple[int, int, list[str]]:
            print(f"Building index from {data_dir} with strategy '{strategy}'...")
            documents, errors = discover_documents(data_dir)
            chunking = chunking or ChunkingConfig(strategy=strategy, embedding_model=self.embedding_model)
            self.build_store(chunking)
            self.chunking = chunking
            document_splits = self.get_document_splits(documents, chunking)

            self._store.reset_collection()
            self._store.add_documents(documents=document_splits)
            print(f"Indexed {len(documents)} documents into {len(document_splits)} passages.")

    def build_store(self, chunking):
        from langchain_ollama import OllamaEmbeddings
        embeddings = OllamaEmbeddings(
            model=chunking.embedding_model or self.embedding_model,
        )
        self.db_dir.mkdir(parents=True, exist_ok=True)
        self._store = Chroma(collection_name="l1_assistant", 
                            persist_directory=str(self.db_dir),
                            embedding_function=embeddings)

    def get_document_splits(self, documents, chunking):
        if chunking.strategy == "recursive":
            return self.recursive_chunk_document(documents, chunking)
        if chunking.strategy == "fixed":
            return self.fixed_chunk_document(documents, chunking)
        if chunking.strategy == "section":
            return self.section_aware_chunk_document(documents, chunking)


    def fixed_chunk_document(self, docs: list[Document], chunking: ChunkingConfig) -> list[Document]:
        text_splitter = CharacterTextSplitter(
          separator="\n\n",
          chunk_size=chunking.chunk_size,
          chunk_overlap=chunking.chunk_overlap,
          strip_whitespace=True,
        )
        all_splits = text_splitter.split_documents(docs)
        return all_splits

    def recursive_chunk_document(self, docs: list[Document], chunking: ChunkingConfig) -> list[Document]:
            text_splitter = RecursiveCharacterTextSplitter(
                separators=list(chunking.separators),
                chunk_size=chunking.chunk_size, 
                chunk_overlap=chunking.chunk_overlap,
                strip_whitespace=True
              )
            all_splits = text_splitter.split_documents(docs)
            return all_splits

    def section_aware_chunk_document(self, docs: list[Document], chunking: ChunkingConfig) -> list[Document]:
          from langchain_text_splitters import MarkdownHeaderTextSplitter
          headers_to_split_on = [
              ("#", "Header 1"),
              ("##", "Header 2"),
              ("###", "Header 3"),
              ("####", "Header 4"),
          ]

          md_header_splits = []
          markdown_splitter = MarkdownHeaderTextSplitter(
              headers_to_split_on=headers_to_split_on, strip_headers=False
          )
          for doc in docs:
            md_header_splits.extend(markdown_splitter.split_text(doc.page_content))

          from langchain_text_splitters import RecursiveCharacterTextSplitter

          chunk_size = chunking.chunk_size
          chunk_overlap = chunking.chunk_overlap
          text_splitter = RecursiveCharacterTextSplitter(
              chunk_size=chunk_size, chunk_overlap=chunk_overlap
          )
          all_splits = text_splitter.split_documents(md_header_splits)
          return list(all_splits)

    def search(self, question):
         
         return self._store.as_retriever().invoke(question)

