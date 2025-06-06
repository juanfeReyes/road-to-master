## Execution commands

### Strategy
1. First build all images that are required for the cluster
2. Run deployment script to allow helm to install charts
3. Run: `minikube tunnel` to expose ingress to windows local host

#### TODO:

**Things to AUTOMATE**
1. Elastic search default user should use a static manually defined password
2. Could be good to find a way to start pet-api and inject the enrollment-token during pod creation
3. Update Keycloak realment settings when deploying pet-api in cluster (view image!) (dev environmen)
4. Allow to have the cluster deployed for development (local environment)
5. Add pet-api to deployment script
6. **Important:** Upgrade deployment script to helm 3 (if possible)

**Fixes:**  
- Fleet server: Execute this commands manually to allow fleet server enrollment
Enter the minikube host: minikube ssh
Enable promiscuous mode on the Docker interface: sudo ip link set docker0 promisc on

`note: https://kubernetes.io/docs/tasks/debug/debug-application/debug-service/#a-pod-fails-to-reach-itself-via-the-service-ip`

- Pods constantly failling: When cluster is running we must assign enough resources:
minikube start --memory 8192 --cpus 3

### Study notes:

#### Ingress

  - Nginx works as an ingress service to expose HTTP/HTTPS and TCP ports to external clients
  - Nginx value.yml file contains the TCP ports to expose, follow documentation
  - To expose ingress rules for services, add the IngressClassName: nginx, as required to update nginx ingress

#### Secrets

  - Secrets can be configured as a configMap which will be stored in pod volume

#### Volumes

  - Need to stody more to be honest

#### Services

  - Used as an entry point to a pod

##### TIPS:

**Get password for elastic user**
kubectl get secret elasticsearch-es-elastic-user -o jsonpath="{.data.elastic}" | base64 -d
