plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("java")
}

group = "com.github.neckbosov.bsc_bosov"

repositories {
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    implementation(libs.kotlin.reflect)

    implementation(libs.kotlinx.serializaion.json)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}