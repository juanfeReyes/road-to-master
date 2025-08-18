
# Lambda API

The project setups follows certain practices as:

## Configuration file

In order to centralize configuration we use `lightweight-config` and a conguration.yml in the resources
folder in order to set configuration variables

## Dependency injection

For dependency injection we use `Guice` that works as a inversion of control container to instance and inject
binded classes

## Logging

For logging we configure `aws-lambda-java-log4j2` and `log4j-slf4j-impl` with log4js.xml to configure
the logging template

## Http client

As a lightweight HTTP client we use feign to implement the clients and perform requests

