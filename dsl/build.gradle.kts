plugins {
    kotlin("jvm")
    id("java")
}

group = "com.github.neckbosov.bsc_bosov"

repositories {
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    kotlin("reflect")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}