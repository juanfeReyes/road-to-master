
## DB migrations
docker run --rm \
-v /path/to/changelog:/liquibase/changelog \
-e LIQUIBASE_COMMAND_URL="jdbc:postgresql://localhost:5432/mydb" \
-e LIQUIBASE_COMMAND_USERNAME="username" \
-e LIQUIBASE_COMMAND_PASSWORD="password" \
liquibase/liquibase:5.0.1 update