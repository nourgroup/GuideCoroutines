buildscript {
    repositories {
        google()
        mavenCentral()
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id 'com.android.application' version '7.3.1' apply false
    id 'com.android.library' version '7.3.1' apply false
    id 'org.jetbrains.kotlin.android' version '1.7.20' apply false
    id 'com.diffplug.spotless' version '6.11.0'
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply plugin: 'com.diffplug.spotless'
    spotless {
        kotlin {
            target '**/*.kt'
            targetExclude("$buildDir/**/*.kt")
            targetExclude('bin/**/*.kt')

            ktlint("0.45.2").userData([android: "true"])
            licenseHeaderFile rootProject.file('spotless/copyright.kt')
        }
    }
}