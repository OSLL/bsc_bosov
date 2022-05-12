plugins {
    id("java")
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.20"
    application
}

group = "com.github.neckbosov.bsc_bosov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.github.neckbosov.bsc_bosov.task_manager.MainKt")
}
dependencies {
    kotlin("stdlib")
    implementation(project(":common"))
    implementation(project(":dsl"))
    implementation(libs.kotlinx.serializaion.json)
    implementation(libs.kotlinx.cli)
    implementation(libs.bundles.ktor.client)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}