FROM openjdk:16-jdk-alpine
COPY target/PetShelter-0.0.1-SNAPSHOT.jar petshelter-api.jar
COPY init-script/apm-agent/elastic-apm-agent-1.34.1.jar agent/elastic-apm-agent-1.34.1.jar
COPY init-script/apm-agent/apm-server-cert.crt agent/apm-server-cert.crt
EXPOSE 8080
ENTRYPOINT ["java","-javaagent:agent/elastic-apm-agent-1.34.1.jar", "-Delastic.apm.service_name=pet-api", "-Delastic.apm.application_packages=com.road.master.PetShelter", "-Delastic.apm.server_urls=http://apm:8200", "-jar", "/petshelter-api.jar"]
