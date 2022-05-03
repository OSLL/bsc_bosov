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

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}