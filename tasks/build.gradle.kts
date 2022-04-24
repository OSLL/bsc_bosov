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
    kotlin("stdlib")
    implementation(project(":dsl"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}