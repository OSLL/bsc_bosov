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
    mainClass.set("com.github.neckbosov.bsc_bosov.server.ApplicationKt")
}

dependencies {
    implementation(project(":dsl"))
    implementation(project(":tasks"))
    implementation(project(":code-mappers"))
    implementation(project(":common"))
    implementation(libs.kotlinx.serializaion.json)

    implementation(libs.kmongo.id)
    implementation(libs.kmongo.coroutine.serialization)

    implementation(libs.bundles.ktor.server)
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.html)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}