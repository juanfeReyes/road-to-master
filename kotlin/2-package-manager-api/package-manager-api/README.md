
# Integration tests
## Requisites
- Rancher desktop 
- Testcontainers desktop (To bridge configuration)

1. Start Rancher Desktop for Docker environment
2. Go into terminal (powershell) and set env variable
```shell
$env:DOCKER_HOST = "npipe:////./pipe/docker_engine"
```
3. Start Test container Desktop to bridge config with between test container and docker


guide: https://docs.rancherdesktop.io/how-to-guides/using-testcontainers/

