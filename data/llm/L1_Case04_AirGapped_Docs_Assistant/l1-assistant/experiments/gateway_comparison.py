"""Report schema for an externally executed local-vs-gateway comparison.

The gateway is intentionally not used by the production assistant. Measurements are
supplied as JSON so the comparison can be run only where policy permits network access.
"""
import argparse
import json
from pathlib import Path


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("measurements", type=Path)
    args = parser.parse_args()
    data = json.loads(args.measurements.read_text(encoding="utf-8"))
    required = {"local", "gateway"}
    missing = required - data.keys()
    if missing:
        raise SystemExit(f"Missing comparison groups: {', '.join(sorted(missing))}")
    for name in ("local", "gateway"):
        row = data[name]
        print(f"{name}: latency={row['latency_ms']}ms cost={row['cost']} quality={row['quality']}")


if __name__ == "__main__":
    main()
