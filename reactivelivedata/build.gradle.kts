plugins {
    kotlin("android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

android {
    namespace = "com.github.musichin.reactivelivedata"

    buildToolsVersion = "33.0.0"
    compileSdk = 33

    defaultConfig {
        minSdk = 14
        targetSdk = compileSdk
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        buildConfig = false
        resValues = false
    }
    compileOptions {
        targetCompatibility(1.8)
        sourceCompatibility(1.8)
    }
}

dependencies {
    api("androidx.lifecycle:lifecycle-livedata:2.6.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.lifecycle:lifecycle-runtime-testing:2.5.1")
}
