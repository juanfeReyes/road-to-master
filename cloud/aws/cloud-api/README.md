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
