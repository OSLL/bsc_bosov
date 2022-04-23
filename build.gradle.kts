import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20" apply false
}

group = "com.github.neckbosov.bsc_bosov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}