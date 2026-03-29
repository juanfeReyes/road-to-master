import java.util.regex.Pattern.compile

plugins {
    id("java")
    id("org.liquibase.gradle") version "3.0.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changelogFile" to "src/main/resources/db/changelog.sql",
            "url" to "jdbc:postgresql://localhost:5432/shipment_db",
            "username" to "postgres",
            "password" to "password",
            "driver" to "org.postgresql.Driver"
        )
    }
    runList = "main"
}

buildscript {
    dependencies {
        classpath("org.liquibase:liquibase-core:5.0.1")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.apache.spark:spark-core_2.13:4.0.1")
    implementation("org.apache.spark:spark-sql_2.13:4.0.1")
    implementation("org.yaml:snakeyaml:2.5")
    implementation("org.postgresql:postgresql:42.7.8")
    liquibaseRuntime("org.liquibase:liquibase-core:5.0.1")
    liquibaseRuntime("info.picocli:picocli:4.7.5")
    liquibaseRuntime("org.postgresql:postgresql:42.7.8")
   }

tasks.test {
    useJUnitPlatform()
}
