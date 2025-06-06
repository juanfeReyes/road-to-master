# Liquibase documentation

For this example we will use docker to execute liquibase commands

`Note:` Run in Powershell. Git bash does not mount correctly


```bash
docker run --network host --rm -v "/$(pwd)/src/main/resources/liquibase":/liquibase/changelog \
 --env-file ./src/main/resources/liquibase/liquibase.env liquibase/liquibase:4.24 \
 --log-level ERROR generate-changelog

```
