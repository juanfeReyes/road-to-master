echo "Seeting roles for deployment"
#kubectl apply -f shared/roles/job-watch-roles.yml
helm repo add codecentric https://codecentric.github.io/helm-charts
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add elastic https://helm.elastic.co

echo "Configuring minikube hairpin"
minikube ssh -- sudo ip link set docker0 promisc on

echo "Starting deployment"
if !(  kubectl get pods -o jsonpath='{range .items[*]}{.status.containerStatuses[*].ready.true}{.metadata.name}{ "\n"}{end}' | grep -q mariadb-release )
then
  echo "Installing MariaDB"
  mariadb_path=$(find . -type d -iname "mariadb")
  helm install -f "${mariadb_path}/values.yml" mariadb-release bitnami/mariadb
  sleep 15s
fi

if !(  kubectl get pods -n ingress-nginx -o jsonpath='{range .items[*]}{.status.containerStatuses[*].ready.true}{.metadata.name}{ "\n"}{end}' | grep -q nginx-controller-release )
then
  echo "Intalling Nginx ingress controller"
  ingress_controller_path=$(find . -type d -iname "nginx-controller")
  helm upgrade nginx-controller-release ingress-nginx --install --repo https://kubernetes.github.io/ingress-nginx --namespace ingress-nginx --create-namespace -f "${ingress_controller_path}/values.yml"
  sleep 60s
fi

if !(  kubectl get pods -o jsonpath='{range .items[*]}{.status.containerStatuses[*].ready.true}{.metadata.name}{ "\n"}{end}' | grep -q keycloak-release )
then
  echo "Installing Keycloak"
  keycloak_path=$(find . -type d -iname "keycloak")
  # Run job to provision keycloak user info
  helm delete pre-keycloak-job
  helm upgrade -i pre-keycloak-job "${keycloak_path}/pre-install" -f "${keycloak_path}/pre-install/values.yml" 
  # TODO: Add init container to wait for pre-job
  kubectl create secret generic keycloak-realm-secret --from-file=shared/keycloak/pet-realm.json
  helm install -f "${keycloak_path}/values.yml" keycloak-release codecentric/keycloak
  sleep 15s
fi

