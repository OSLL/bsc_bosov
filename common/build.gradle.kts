plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("java")
}

group = "com.github.neckbosov.bsc_bosov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":ir"))
    implementation(libs.kotlinx.serializaion.json)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}