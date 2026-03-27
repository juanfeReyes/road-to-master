plugins {
    id("java")
    id("scala")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
//    compileOnly(libs.scala.library)
    implementation("org.scala-lang:scala-library:2.12.21")
    implementation("io.github.data-catering:data-caterer-api:0.19.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    implementation("org.apache.spark:spark-core_2.13:4.0.1")
//    implementation("org.apache.spark:spark-sql_2.13:4.0.1")
//    implementation("org.yaml:snakeyaml:2.5")
//    implementation("org.postgresql:postgresql:42.7.8")
}

tasks.test {
    useJUnitPlatform()
}