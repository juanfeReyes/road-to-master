

Run to update the db schema:

```bash
docker run --network host --rm -v "$(pwd)/liquibase":/liquibase/changelog liquibase/liquibase:4.6 \
--defaultsFile="/liquibase/changelog/liquibase.docker.properties" --log-level ERROR update
```

Generate schema command:


for local environment
```bash
docker run --network host --rm -v "$(pwd)/liquibase":/liquibase/changelog liquibase/liquibase:4.6 \
--defaultsFile="/liquibase/changelog/liquibase.docker.properties" --changeLogFile=/liquibase/changelog/master_changelog.json generateChangeLog
```

for stage environment
```bash
docker run --network host --rm -v "$(pwd)/liquibase":/liquibase/changelog liquibase/liquibase:4.6 \
--defaultsFile="/liquibase/changelog/liquibase-stage.docker.properties" --changeLogFile=/liquibase/changelog/master_changelog.json generateChangeLog
```
