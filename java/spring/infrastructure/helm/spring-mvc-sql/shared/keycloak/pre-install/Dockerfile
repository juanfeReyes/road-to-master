FROM liquibase/liquibase:4.6
ADD /changelog /liquibase/changelog
CMD ["sh","-c", "docker-entrypoint.sh --defaultsFile=/liquibase/changelog/liquibase.docker.properties update"] 
