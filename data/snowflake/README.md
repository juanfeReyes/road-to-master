# Snowflake Basics

## Setup 

Install SnowSQL client to connect to the data warehouse [download](https://www.snowflake.com/en/developers/downloads/snowsql/)

## Login

test connection by running the following command
```sh
snowsql -a <account_name> -u <login_name>

!exit
```

configure access by creating file <user_profile>/.snowsql/config

```conf
accountname = <account_name>
username = <login_name>
password = <password>
```

then run the following command to connect to SnowFlake:
```sh
snowsql
```


