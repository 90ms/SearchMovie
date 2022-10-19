import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec

fun PluginDependenciesSpec.androidApp() =
    id("com.android.application")

fun PluginDependenciesSpec.androidLibrary() =
    id("com.android.library")

fun PluginDependenciesSpec.kotlinAndroid() =
    kotlin("android")

fun PluginDependenciesSpec.kotlinParcelize() =
    id("kotlin-parcelize")

fun PluginDependenciesSpec.kotlinKapt() =
    kotlin("kapt")

fun PluginDependenciesSpec.ktlint() =
    id("org.jlleitschuh.gradle.ktlint")

fun PluginDependenciesSpec.daggerHilt() =
    id("dagger.hilt.android.plugin")

fun PluginDependenciesSpec.junit5() =
    id("de.mannodermaus.android-junit5")
