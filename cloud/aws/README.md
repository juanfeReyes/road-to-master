# r2m-cloud
Road to Master cloud

### AWS CLI
login: 
```
aws sso login
```

### Light Sail
1. Start VPS with light sail
  a. Start linux instance
  b. Download .pem key
  c. ssh to machine

2. Build cloud-api and cloud-ui
  a. cloud-api: mvn clean install -DskipTests
  b. cloud-ui: npm run build

3. Distribute, copy artifacts to VPS
  a. sudo scp -i LightsailDefaultKey-us-east-1.pem cloud-api.jar   ec2-user@54.81.241.8:/home/ec2-user
  b. sudo scp -i LightsailDefaultKey-us-east-1.pem index.html ec2-user@54.81.241.8:/home/ec2-user
  c. sudo scp -i LightsailDefaultKey-us-east-1.pem _next.zip  ec2-user@54.81.241.8:/home/ec2-user

4. Config VPS
  a. install nginx
  b. install java 17
  c. config nginx proxy
  d. move cloud-ui to nginx folder
  e. reload nginx config: sudo nginx -s reload
  f. start cloud-api

