import java.net.URI

buildscript {
    repositories {
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT}")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:${Versions.JUNIT5}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}")
    }
}

allprojects {
    repositories {
        google()
        maven {
            url = URI("https://jitpack.io")
        }
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
