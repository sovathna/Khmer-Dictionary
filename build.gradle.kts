// Top-level build file where you can add configuration options common to all sub-projects/modules.
val hiltVersion by extra("2.51.1")

plugins {
  id("com.android.application") version "8.5.1" apply false
  id("com.android.library") version "8.5.1" apply false
  id("org.jetbrains.kotlin.android") version "1.9.23" apply false
  id("com.google.dagger.hilt.android") version "2.51.1" apply false
  id("androidx.navigation.safeargs") version "2.7.7" apply false
//  id("com.google.gms.google-services") version "4.4.2" apply false
//  id("com.google.firebase.crashlytics") version "3.0.2" apply false
//  id("com.google.firebase.firebase-perf") version "1.4.2" apply false
  id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
}
