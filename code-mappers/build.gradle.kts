plugins {
    id("java")
    kotlin("jvm")
}

group = "com.github.neckbosov.bsc_bosov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ir"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}