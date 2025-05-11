// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.1" apply false  // Movido para cá com apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
//    id("com.google.relay") version "0.3.12" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
