import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("android")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

android {
    buildToolsVersion = "31.0.0"
    compileSdk = 31

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
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.lifecycle:lifecycle-runtime-testing:2.3.1")
}

mavenPublish {
    sonatypeHost = SonatypeHost.S01
}
