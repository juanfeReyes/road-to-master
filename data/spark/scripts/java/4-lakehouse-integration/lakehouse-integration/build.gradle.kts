plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.apache.spark:spark-core_2.13:4.0.1")
    implementation("org.apache.spark:spark-sql_2.13:4.0.1")
    implementation("org.yaml:snakeyaml:2.5")
    implementation("org.postgresql:postgresql:42.7.8")
    implementation("org.apache.iceberg:iceberg-spark-runtime-4.0_2.13:1.10.1")
//    implementation("org.apache.hadoop:hadoop-client:3.4.2")
}

tasks.test {
    useJUnitPlatform()
}