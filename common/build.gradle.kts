plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.20"
    id("java")
}

group = "com.github.neckbosov.bsc_bosov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":dsl"))
    implementation(libs.kotlinx.serializaion.json)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}