Bank broker challenge

![Bank-9](https://user-images.githubusercontent.com/92064684/236699805-a945aadd-3bab-4366-9ffb-e05c9c591fce.jpg)

#### LDAP:

For setup: http://localhost:8085/setup/
username: admin-admin
pass: adminLDAP1*

for users
bankBroker9*


## To export realm keycloak:

docker exec -it  <container-id>  /bin/bash
run the following command inside docker container
```bash
/opt/jboss/keycloak/bin/standalone.sh \
-Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
-Dkeycloak.migration.realmName=bank-broker \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/tmp/bank-broker-realm.json
```
