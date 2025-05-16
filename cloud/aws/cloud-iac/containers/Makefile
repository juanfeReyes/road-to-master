
.PHONY: push_ui_image

clean: clean_ecs clean_ecr

deploy_ecr: deploy_ecr_repo build_ui push_ui_image build_api push_api_image

build_ui:
	@echo "Build UI Image"
	cd '/mnt/d/road to master/r2m-cloud/cloud-ui'; \
		docker.exe build . -t r2m-containers-repository/cloud-ui --no-cache

build_api:
	@echo "Build API Image"
	cd '/mnt/d/road to master/r2m-cloud/cloud-api'; \
		docker.exe build . -t r2m-containers-repository/cloud-api --no-cache

deploy_ecr_repo:
	@echo "Deploy ECR repository"
	tofu.exe -chdir=ecr/ apply --auto-approve

push_ui_image:
	$(eval ECR_REPO := $(shell tofu.exe -chdir=ecr/ output -json | jq -r ".ecr_repository_url.value" | awk -F"/" '{print $1}'))
	aws.exe ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $(ECR_REPO)
	cd '/mnt/d/road to master/r2m-cloud/cloud-ui'; \
		docker.exe tag r2m-containers-repository/cloud-ui:latest $(ECR_REPO):cloud-ui
	docker.exe push $(ECR_REPO):cloud-ui

push_api_image:
	$(eval ECR_REPO := $(shell tofu.exe -chdir=ecr/ output -json | jq -r ".ecr_repository_url.value" | awk -F"/" '{print $1}'))
	aws.exe ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $(ECR_REPO)
	cd '/mnt/d/road to master/r2m-cloud/cloud-api'; \
		docker.exe tag r2m-containers-repository/cloud-api:latest $(ECR_REPO):cloud-api
	docker.exe push $(ECR_REPO):cloud-api


deploy_ecs_cluster:
	@echo "Deploy ECS cluster"
	tofu.exe -chdir=ecs-cluster/ apply --auto-approve


clean_ecr:
	tofu.exe -chdir=ecr/ destroy --auto-approve

clean_ecs:
	tofu.exe -chdir=ecs-cluster/ destroy --auto-approve

