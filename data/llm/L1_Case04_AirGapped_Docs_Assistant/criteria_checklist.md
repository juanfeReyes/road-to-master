# Submission Checklist — L1_Case04_AirGapped_Docs_Assistant

## Deliverables

- [ ] Working assistant over all three documents, answering every question in `data/engineer_questions.csv` with a document and section citation.
- [ ] **Chunking experiment**: at least two strategies, retrieval quality scored per question on whether the retrieved context contained what was needed, a table of results, and the questions each strategy got wrong.
- [ ] **Local-versus-gateway comparison**: the same pipeline with Ollama and through Portkey, measured latency, cost and answer quality on the same questions.
- [ ] A recommendation for the air-gapped client that **states what the local option costs in quality**.
- [ ] Your account of the questions the assistant gets wrong, and why.
- [ ] A one-page design note, a short demo, and a declared-effort statement.

## Evidence standard

Every claim cites a question id, a document section, or a measured number. "Semantic
chunking performed better" scores nothing. "Section-level chunking answered 12 of 15;
fixed 400-token chunking answered 9, failing Q03, Q07 and Q08 because the safety
section was never retrieved alongside the procedure" scores.

Retrieval quality means: did the retrieved context contain what was needed? An
answer that is correct because the model already knows about turbines is not
evidence that retrieval works, and reporting answer accuracy alone will be read as
not having measured retrieval at all.

## Before you submit — challenge your own work

- [ ] **Where does this manual state its safety requirements, and how many times?** What does that mean for any chunking strategy that treats a procedure as a self-contained unit?
- [ ] For each procedure question: did the retrieved context include the constraints that govern that procedure, or only its steps?
- [ ] The three documents disagree in places. For each disagreement, *why* do they disagree — and does the reason determine which value is correct? Is "prefer the newest document" ever wrong here?
- [ ] Is there a question where the right answer is a specific figure that appears twice in the corpus with different values? Which did I return?
- [ ] Is there a question the corpus cannot answer at all? Did I decline, or invent something plausible?
- [ ] Is there a question the corpus explicitly places out of scope? Did I decline *and* point to the right document?
- [ ] Did anything in the documents change how my assistant behaved, as opposed to what it retrieved?
- [ ] Does my local-versus-gateway recommendation admit a downside? If not, why should anyone believe it?

## How this will be assessed

Your assistant is run against a **held-out question set you have not seen**, over
the same corpus, covering the same difficulties in different words.

The safety-context questions are weighted heavily. An assistant that answers a
procedure question without the constraints governing that procedure has produced a
dangerous answer rather than an incomplete one, and it will be scored that way.

Both experiments must be measured. A described experiment with no numbers scores
zero on that criterion.

You will also answer several questions about your own submission at submission time.
