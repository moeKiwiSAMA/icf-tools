import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.20"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    application
}

group = "cat.kiwi"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("cat.kiwi.game.icf.generator.Application")
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
}

kotlin {
    jvmToolchain(8)
}


