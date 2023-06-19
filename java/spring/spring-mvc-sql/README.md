# pet-shelter-road-to-master

## Build docker image
To build the image run the following command:
```bash
docker build -t pet-shelter-api .
```

To start the container, add the following to docker compose
```bash
pet-api:
    build: .
    depends_on:
      - mariadb
      - kafka
      - keycloak
    ports:
      - 8080:8080
 ```

## To export realm keycloak:

docker run -it <image> bash
run the following command inside docker container
```bash
/opt/jboss/keycloak/bin/standalone.sh \
-Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
-Dkeycloak.migration.realmName=pets-api-realm \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/tmp/pet-realm.json
```
