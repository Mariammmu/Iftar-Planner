// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
//    id ("androidx.navigation.safeargs")
//    id ("com.google.gms.google-services")
}