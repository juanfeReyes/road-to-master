
```sh
docker build --no-cache -t data-caterer:latest .
```

```shell
docker run -d -e PLAN_CLASS=com.r2m.data.caterer.plans.plans.PackagePlan -e DEPLOY_MODE=client  data-caterer:latest
```

specify the network of your DB: 
```shell
 docker run -e PLAN_CLASS=com.r2m.data.caterer.plans.plans.PackagePlan -e DEPLOY_MODE=client --network=spark_default data-caterer:latest
```