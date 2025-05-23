pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io") // ADICIONE AQUI TAMBÉM
    }
    /*plugins {
        id("org.jetbrains.kotlin.jvm") version "1.9.23"
        id("kotlin-kapt") version "1.9.23"
        id("dagger.hilt.android.plugin") version "2.51"
    }*/
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "devMobile-gym"
include(":app")
