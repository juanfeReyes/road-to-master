# Feature Specification: Simple RAG Agent

**Feature Branch**: `001-simple-rag-agent`

**Created**: 2026-09-03

**Status**: Draft

**Input**: User description: "build a simple rag agent with python in the src folder capable of embedding the md files in the data folder and finally track the score of the response and print a score"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Ask Questions About Local Documentation (Priority: P1)

As a user working in an air-gapped environment, I want to ask a question and receive an answer grounded in the Markdown documents in the data folder so that I can retrieve useful information without internet access.

**Why this priority**: Answering questions from the available documentation is the core value of the assistant.

**Independent Test**: Place the provided Markdown documents in the designated data folder, ask a question whose answer appears in one document, and verify that the response addresses the question using that documentation.

**Acceptance Scenarios**:

1. **Given** the data folder contains readable Markdown documents, **When** the user submits a question, **Then** the assistant returns an answer based on the most relevant document content.
2. **Given** the data folder contains no relevant content, **When** the user submits a question, **Then** the assistant clearly states that the documentation does not provide enough information rather than presenting an unsupported answer.
3. **Given** the assistant is run without network access, **When** the user submits a question, **Then** the assistant completes the request without requiring an external service.

---

### User Story 2 - Keep the Document Index Current (Priority: P2)

As a maintainer, I want the assistant to discover and embed all Markdown files in the data folder so that newly added documentation can be used for questions.

**Why this priority**: A reliable document index is necessary for answers to reflect the current local knowledge base.

**Independent Test**: Add, modify, and remove Markdown files in the data folder, rebuild the index, and verify that searches reflect the resulting set of documents.

**Acceptance Scenarios**:

1. **Given** the data folder contains multiple Markdown files, **When** the index is built, **Then** each readable Markdown file is represented in the searchable index.
2. **Given** a Markdown file is changed, **When** the index is rebuilt, **Then** subsequent answers use the changed content.
3. **Given** a file is unreadable or malformed, **When** the index is built, **Then** the assistant reports the file problem and continues processing other readable Markdown files.

---

### User Story 3 - Inspect Response Quality (Priority: P3)

As a user or evaluator, I want the assistant to track and print a score for each response so that I can assess answer quality over time.

**Why this priority**: A visible quality signal supports evaluation and iteration while keeping the question-answering flow useful.

**Independent Test**: Ask a question with known supporting documentation and verify that the output includes a numeric score with a consistent scale and an understandable label.

**Acceptance Scenarios**:

1. **Given** a response has been generated, **When** the assistant finishes the request, **Then** it prints a numeric response-quality score and the scale used.
2. **Given** retrieved documentation strongly supports the response, **When** the response is scored, **Then** the score is higher than for a response with weak or missing supporting documentation.
3. **Given** scoring cannot be completed, **When** the response is printed, **Then** the assistant identifies the score as unavailable instead of presenting a fabricated value.

### Edge Cases

- The data folder is missing, empty, or contains no Markdown files.
- A Markdown file uses an unexpected encoding or cannot be read.
- A question is blank or contains only whitespace.
- No indexed passage meets the minimum relevance needed to ground an answer.
- The question or retrieved content is substantially larger than the supported input size.
- The generated response contains no supporting evidence from the retrieved passages.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The assistant MUST run locally without requiring network connectivity or remote document storage.
- **FR-002**: The assistant MUST discover readable `.md` files under the designated data folder.
- **FR-003**: The assistant MUST transform discovered document content into searchable representations before answering questions.
- **FR-004**: The assistant MUST split document content into retrievable passages while preserving enough source information to identify the originating file.
- **FR-005**: The assistant MUST retrieve the passages most relevant to a submitted question.
- **FR-006**: The assistant MUST generate an answer using retrieved passages and MUST avoid asserting unsupported facts when relevant evidence is unavailable.
- **FR-007**: The assistant MUST provide the source file names used for each grounded answer.
- **FR-008**: The assistant MUST accept a question through the project's supported user interaction and print the resulting answer.
- **FR-009**: The assistant MUST calculate a response-quality score based on evidence relevance and answer support, using a documented consistent numeric scale.
- **FR-010**: The assistant MUST print the score alongside each response, including the score scale and an indication when scoring is unavailable.
- **FR-011**: The assistant MUST report actionable errors for missing data, invalid input, and document-processing failures without hiding failures.
- **FR-012**: Rebuilding the searchable representations MUST reflect additions, modifications, and removals in the data folder.

### Key Entities

- **Source Document**: A readable Markdown file in the data folder, identified by its path and content.
- **Document Passage**: A searchable segment of a source document that retains its source reference and text.
- **User Question**: The natural-language request submitted to the assistant.
- **Grounded Response**: An answer generated from retrieved document passages, including source references.
- **Response Score**: A numeric indication of how well the grounded response is supported by relevant retrieved content.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: In an offline run using the supplied documents, at least 90% of test questions with answers present in the documents receive a response containing the correct supporting source.
- **SC-002**: For a knowledge base of up to 100 Markdown files, a user receives an answer and printed score within 10 seconds after submitting a question, excluding one-time index creation.
- **SC-003**: 100% of completed responses print a numeric score on the documented scale or explicitly print that the score is unavailable.
- **SC-004**: At least 90% of evaluation questions with no supporting documentation receive an explicit insufficient-evidence response rather than an invented factual answer.
- **SC-005**: A maintainer can rebuild the index after changing the data folder and observe the change in search results without modifying application source code.

## Assumptions

- The initial audience is a single local user operating the assistant from the project workspace.
- The data folder is the authoritative source of Markdown documentation for the first release.
- A rebuild operation is acceptable when documents are added, changed, or removed; continuous file watching is out of scope.
- The response score is an evaluation signal, not a guarantee of factual correctness.
- The first release supports text questions and Markdown documents only; authentication, multi-user access, and non-Markdown formats are out of scope.
- The assistant is integrated into the existing project source area and uses the project's available local runtime and dependencies.
