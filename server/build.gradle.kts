plugins {
    id("java")
    kotlin("jvm")
    application
}

group = "com.github.neckbosov.bsc_bosov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.github.neckbosov.bsc_bosov.server.ApplicationKt")
}

dependencies {
    implementation(project(":dsl"))
    implementation(project(":tasks"))
    implementation(project(":code-mappers"))
    implementation("io.ktor:ktor-server-core:2.0.0")
    implementation("io.ktor:ktor-server-jetty:2.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}