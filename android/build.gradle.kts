// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    //safeArgs
//    id ("androidx.navigation.safeargs") version "2.4.2" apply false
    //hilt
    id ("com.google.dagger.hilt.android") version "2.44" apply false
    id ("com.android.library") version "7.4.1" apply false
}