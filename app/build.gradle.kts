plugins {
    alias(libs.plugins.android.application)
    id("androidx.navigation.safeargs")
    id("com.google.gms.google-services") // Use only this
}

android {
    namespace = "com.mariammuhammad.iftarplanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mariammuhammad.iftarplanner"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.google.code.gson:gson:2.12.1")

    implementation ("com.airbnb.android:lottie:6.6.2")

    implementation("androidx.cardview:cardview:1.0.0")

    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation ("io.reactivex.rxjava3:rxjava:3.1.6")

    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.11.0")
    implementation ("androidx.room:room-rxjava3:2.6.1")
//
//    implementation ("androidx.navigation:navigation-fragment:2.7.7")
//    implementation ("androidx.navigation:navigation-ui:2.7.7")

   // implementation platform('com.google.firebase:firebase-bom:33.8.0')
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
//    implementation("com.google.firebase:firebase-database")
//    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:21.1.0")

    implementation("com.google.android.gms:play-services-auth:21.3.0")


    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.firebase:firebase-database")

}
