plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("dagger.hilt.android.plugin")
//    id ("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.lessgenius.maengland"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lessgenius.maengland"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // Standard Wear OS libraries
    implementation("androidx.wear:wear:1.3.0")
    // includes support for wearable specific inputs
    implementation("androidx.wear:wear-input:1.1.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.3.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.activity:activity-ktx:1.7.2")

    // liveData
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")

    // framework ktx
    implementation ("androidx.fragment:fragment-ktx:1.6.0")

    // Jetpack Navigation Kotlin
//    val nav_version = "2.4.2"
//    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
//    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")

    // material ui
    implementation ("com.google.android.material:material:1.5.0")

    // glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    // hilt
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-compiler:2.44")

    // room
    val room_version = "2.4.3"
    implementation ("androidx.room:room-runtime:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")

    // sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    // lottie
    implementation ("com.airbnb.android:lottie:6.1.0")
}