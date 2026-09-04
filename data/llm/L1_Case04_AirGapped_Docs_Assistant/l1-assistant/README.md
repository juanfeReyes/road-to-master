Offline Markdown RAG assistant

## Usage

Provision the configured local embedding and chat model artifacts before disconnecting
from the network:

```powershell
uv run l1-assistant index --data-dir ..\data --db-dir .\var\chroma
uv run l1-assistant ask "What safety checks are required before changing the liner?" --data-dir ..\data --db-dir .\var\chroma
```

Answers include source filenames and an evidence-support score from `0.00` to `1.00`.
Questions without supporting passages are explicitly declined. Set `L1_EMBEDDING_MODEL`,
`L1_CHAT_MODEL`, `L1_DATA_DIR`, and `L1_DB_DIR` to configure local paths/models.

## Development

```powershell
uv run pytest
```

The retrieval experiment and its reporting requirements are documented in
`EXPERIMENT.md`.

1. Install uv  
```sh
powershell -ExecutionPolicy ByPass -c "irm https://astral.sh/uv/install.ps1 | iex"
```

2. Install spec kit from GitHub  
```sh
uv tool install specify-cli --from git+https://github.com/github/spec-kit.git
```

3. Create project with uv  
```sh
 uv init l1-assistant
```

4. Setup specify   
```sh
specify init --here --integration copilot
```
