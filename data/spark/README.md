
# Apache spark

## Config
### Postgres driver
1. Download driver from: https://jdbc.postgresql.org/download/
2. Copy in data driver: `data\drivers`

## Run cluster
```sh
docker-compose -f docker-compose.yml up -d
```

## SSH to spark master
```sh
docker exec -it spark-master bash
```

## Submit application

```sh
./bin/spark-submit ./scripts/1.spark-basics.py
```

```sh
./bin/spark-submit  --driver-class-path data/drivers/postgresql-42.7.8.jar --jars data/drivers/postgresql-42.7.8.jar ./scripts/2.db-connection.py
```
