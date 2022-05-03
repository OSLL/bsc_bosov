plugins {
    id("java")
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.20"
}

group = "com.github.neckbosov.bsc_bosov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    implementation(project(":dsl"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}