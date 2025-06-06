# Build Images required for cluster deployment

# repositories
# helm repo add codecentric https://codecentric.github.io/helm-charts
# helm repo add bitnami https://charts.bitnami.com/bitnami


echo "Build images process started"

if [-d "tmp"]; then rm -rf /tmp; fi
mkdir tmp

#docker build --no-cache -t pets-infra/keycloak-provisioning ./shared/keycloak/pre-install
docker pull quay.io/keycloak/keycloak:17.0.1-legacy
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.4.2
docker pull docker.elastic.co/kibana/kibana:8.4.2
docker pull docker.io/bitnami/schema-registry:7.2.1-debian-11-r10
docker pull docker.elastic.co/beats/elastic-agent:8.4.2

# If cluster in minikube then load images
#minikube image load pets-infra/keycloak-provisioning --overwrite
minikube image load quay.io/keycloak/keycloak
minikube image load docker.elastic.co/elasticsearch/elasticsearch:8.4.2
minikube image load docker.elastic.co/kibana/kibana:8.4.2
minikube image load docker.io/bitnami/schema-registry:7.2.1-debian-11-r10
minikube image load docker.elastic.co/beats/elastic-agent:8.4.2
