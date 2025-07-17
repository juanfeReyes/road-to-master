
## IDE

download extension: SQL notebooks to see all docs

## Run
```sh
docker-compose.exe up -d
```

## PgAdmin

```sh
docker run -p 80:80 `
    --name pgadmin `
    -e 'PGADMIN_DEFAULT_EMAIL=test@test.com' `
    -e 'PGADMIN_DEFAULT_PASSWORD=test' `
    -d dpage/pgadmin4
```
