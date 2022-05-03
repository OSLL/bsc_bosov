plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.20"
    id("java")
}

group = "com.github.neckbosov.bsc_bosov"

repositories {
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    kotlin("reflect")

    implementation(libs.kotlinx.serializaion.json)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}