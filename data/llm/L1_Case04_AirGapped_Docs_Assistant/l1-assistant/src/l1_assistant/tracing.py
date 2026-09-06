import os
from contextlib import contextmanager
from typing import Any, Iterator


def configure_offline() -> None:
    """Disable hosted evaluation integrations for an air-gapped run."""
    os.environ.setdefault("DEEPEVAL_TELEMETRY_OPT_OUT", "YES")
    os.environ.setdefault("DEEPEVAL_DISABLE_TELEMETRY", "1")


@contextmanager
def evaluation_trace(enabled: bool, name: str) -> Iterator[dict[str, Any]]:
    configure_offline()
    metadata: dict[str, Any] = {"name": name, "enabled": enabled}
    if enabled:
        try:
            from deepeval.tracing import trace
            with trace(name=name):
                yield metadata
        except (ImportError, RuntimeError):
            yield metadata
    else:
        yield metadata
