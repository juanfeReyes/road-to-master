plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "2.2.21"
    kotlin("kapt") version "2.3.20"
    id("com.google.devtools.ksp") version "2.3.5"
}

group = "com.r2m"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-liquibase")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.postgresql:postgresql:42.1.4")
    implementation("com.linecorp.kotlin-jdsl:kotlin-jdsl:3.4.0")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.8.1")
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.8.1")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.8.1")
    implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:0.1.2")
    implementation("org.mapstruct:mapstruct:1.6.3")
    kapt("org.mapstruct:mapstruct-processor:1.6.3")
    implementation("io.mcarle:konvert-api:4.5.0")
    ksp("io.mcarle:konvert:4.5.0")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-liquibase-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers:2.0.4")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter:2.0.4")
    testImplementation("org.testcontainers:testcontainers-postgresql:2.0.4")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.willowtreeapps.assertk:assertk:0.28.1")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.kotlin.semver)
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
