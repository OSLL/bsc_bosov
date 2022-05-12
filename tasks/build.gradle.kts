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
    implementation(project(":common"))
    implementation(project(":dsl"))
    implementation(libs.kotlinx.serializaion.json)


    implementation(libs.bundles.ktor.client)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}