# Liquibase documentation

For this example we will use docker to execute liquibase commands

`Note:` If using docker WSL execute the following command within ubuntu distro:

```bash
docker run --network host --rm -v "$(pwd)/liquibase":/liquibase/changelog liquibase/liquibase:4.6 \
--defaultsFile="/liquibase/changelog/liquibase.docker.properties" --log-level ERROR update
```
