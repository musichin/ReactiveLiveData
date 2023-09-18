buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.10"))
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.25.3")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
