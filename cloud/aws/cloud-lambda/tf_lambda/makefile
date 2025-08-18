.PHONY: all build_lambda provision_lambda

all: build_lambda provision_lambda clean

deploy: build_lambda provision_lambda

currentDir := $(shell pwd)
parentDir := $(shell pwd | sed "s/cloud\/.*$///")
lambdaDir := $(shell find $(parentDir) -type d -name "lambda-setup" | head -n 1)
zipFileName := r2m_lambda.zip

build_lambda:
	@echo "Building lambda"
	@echo $(parentDir)
	@echo $(lambdaDir)
	cd $(lambdaDir) && sam build
	cd $(lambdaDir)/.aws-sam/build/HelloWorldFunction && zip -r $(zipFileName) .
	mv $(lambdaDir)/.aws-sam/build/HelloWorldFunction/$(zipFileName) ./$(zipFileName)
# tar might not be ziping all well

provision_lambda:
	@echo $(parentDir)
	@echo $(lambdaDir)
	tofu.exe apply -var="bucket_source=./$(zipFileName)" --auto-approve
	rm -f ./$(zipFileName)

clean:
	@echo $(parentDir)
	@echo $(lambdaDir)
	tofu.exe destroy -var="bucket_source=./$(zipFileName)" --auto-approve