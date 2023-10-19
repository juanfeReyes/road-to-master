
## SSL

## Generate Certificate
1. openssl genrsa -des3 -out server.key 2048
2. openssl req -new -key server.key -out server.csr
3. create v3.ext file and add SANs
4. openssl x509 -req -in server.csr -signkey server.key -out server.crt -days 3650 -sha256 -extfile v3.ext
5. openssl pkcs12 -export -out server.p12 -inkey server.key -in server.crt

cert password: 123456

After generating the PKCS12 certtificate, add the certificate to java keystore certs:

```shell
keytool.exe -v -importkeystore -srckeystore "\resources\ssl\server.p12" -srcstoretype PKCS12 -destkeystore "\openjdk-20.0.2\lib\security\cacerts" -deststoretype JKS
```