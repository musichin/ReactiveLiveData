plugins {
    kotlin("android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.kotlinx.kover") version "0.7.0"
}

android {
    namespace = "com.github.musichin.reactivelivedata"

    compileSdk = 33

    defaultConfig {
        minSdk = 14
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
        targetCompatibility(17)
        sourceCompatibility(17)
    }
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.lifecycle:lifecycle-runtime-testing:2.6.2")
}
