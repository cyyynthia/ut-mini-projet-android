// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    var kotlinVersion = "2.1.10"

    id("com.android.application") version "8.7.3" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion apply false

    kotlin("plugin.serialization") version kotlinVersion apply false
}
