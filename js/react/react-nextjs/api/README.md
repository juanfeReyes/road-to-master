
Credentials
user: admin@admin.com
pass: adminadmin

To start pocketbase with docker

```
docker run -d \
  --name=pocketbase \
  -p 8090:8090 \
  -v "${PWD}/pb-data:/pb_data" \
  -v "${PWD}/pb-public:/pb_public" \
  --restart unless-stopped \
  ghcr.io/muchobien/pocketbase:latest
```

For windows with Docker desktop use powershell:

```
docker run -d `
  --name=pocketbase `
  -p 8090:8090 `
  -v "${PWD}/pb-data:/pb_data" `
  -v "${PWD}/pb-public:/pb_public" `
  --restart unless-stopped `
  ghcr.io/muchobien/pocketbase:latest
```
