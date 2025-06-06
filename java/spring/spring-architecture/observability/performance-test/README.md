## Execution
To execute k6 use docker with:

```bash
docker run --rm -v ${PWD}:/src -i grafana/k6 run /src/load-get-warehouse.js

```