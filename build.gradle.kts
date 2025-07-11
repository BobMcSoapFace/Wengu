// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    kotlin("kapt") version "1.9.0"
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"
    alias(libs.plugins.compose.compiler) apply false
}
