
.PHONY: all build_api build_ui deploy_infra setup_ssh provision_api provision_ui

all: build_api build_ui deploy_infra setup_ssh provision_api provision_ui

build_api:
	@echo "Building cloud-api"
	cd '/mnt/d/road to master/r2m-cloud/cloud-api'; \
	./mvnw clean install -DskipTests

build_ui:
	@echo "Building cloud-ui"
	cd '/mnt/d/road to master/r2m-cloud/cloud-ui'; \
	npm run build

deploy_infra:
	@echo "Running openTofu to deploy infrastructure in AWS"
	tofu.exe apply --auto-approve
	sleep 20

setup_ssh:
	@echo "Setup key to ssh agent"
	eval $(ssh-agent -s)
	ssh-add '/mnt/d/road to master/r2m-cloud-key.pem'

provision_api:
	@echo "Running Ansible to provision cloud-api"
	ansible-playbook -i playbook/inventory/nodes/hosts.yml playbook/playbook-api.yml

provision_ui:
	@echo "Running Ansible to provision cloud-ui"
	ansible-playbook -i playbook/inventory/nodes/hosts.yml playbook/playbook-web.yml

clean:
	@echo "Running openTofu to deploy infrastructure in AWS"
	tofu.exe destroy --auto-approve
