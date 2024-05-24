buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.0" apply false
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.5.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
}