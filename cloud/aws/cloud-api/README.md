# Cloud API service
This service is to practice integration with cloud providers

#### Compilation
````shell
mvn clean install -DskipTests
````

#### Execution
Go to target folder in root of project
````shell
java -jar cloud-api.jar
````

#### Distribute

Copy jar to instance:
````shell
sudo scp -i "r2m-cloud-key.pem" cloud-api.jar ec2-user@ec2-54-159-23-93.compute-1.amazonaws.com:/home/ec2-user
sudo java -jar cloud-api.jar
````

Run EC2 instance:
````shell
aws ec2 run-instances --image-id ami-01816d07b1128cd2d  --count 1 --instance-type t2.micro --key-name r2m-cloud-key --security-group-ids sg-0ad1d8c7dc99d86a2
````

Terminate EC2 instance:
````shell
aws ec2 terminate-instances --instance-ids i-0e4ba9bf957dbc7e6 --profile PowerUserAccess-
````

## Containerize
Build image:
````shell
docker build . -t r2m/cloud-api --no-cache
````

`note:` The ecr url change per repository
tag for AWS ECR:
````shell
docker tag r2m/cloud-api 140023377333.dkr.ecr.us-east-1.amazonaws.com/r2m/cloud:cloud-api
````

push to ECR:
Login to aws sso
Get login credentials for Docker CLI
`````shell
docker push 140023377333.dkr.ecr.us-east-1.amazonaws.com/r2m/cloud:cloud-api
`````

Test health check in local:
````shell
docker run -dt -p 8080:8080 --name cloud-api --health-cmd "wget --spider http://localhost:8080/api/actuator/health || exit 1" --health-interval=5s --health-retries=5 140023377333.dkr.ecr.us-east-1.amazonaws.com/r2m/cloud:cloud-api
````
