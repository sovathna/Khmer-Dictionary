//import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-parcelize")
  id("com.google.dagger.hilt.android")
  id("androidx.navigation.safeargs.kotlin")
  id("com.google.devtools.ksp")
  //  id("com.google.firebase.crashlytics")
//  id("com.google.firebase.firebase-perf")
//  id("com.google.gms.google-services")
}

android {
  namespace = "io.github.sovathna.khmerdictionary"
  compileSdk = 34

  signingConfigs {
    getByName("debug") {
      storeFile = rootProject.file("debug.jks")
      storePassword = "android"
      keyAlias = "debug"
      keyPassword = "android"
    }
  }
  defaultConfig {
    applicationId = "io.github.sovathna.khmerdictionary"
    minSdk = 23
    targetSdk = 34
    versionCode = 1
    versionName = "1.0.0"
    resourceConfigurations += listOf("en")
    signingConfig = signingConfigs["debug"]
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  bundle {
    storeArchive {
      enable = false
    }
  }

  buildTypes {
    debug {
      isMinifyEnabled = false
      isShrinkResources = false
//      configure<CrashlyticsExtension> {
//        mappingFileUploadEnabled = false
//      }
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    release {
      isMinifyEnabled = true
      isShrinkResources = true
//      configure<CrashlyticsExtension> {
//        mappingFileUploadEnabled = true
//      }
      multiDexKeepProguard = file("multidex-config.pro")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_11.toString()
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }
}

repositories {
  google()
  mavenCentral()
  maven(url = "https://jitpack.io")
}

dependencies {
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")

  implementation("androidx.fragment:fragment-ktx:1.8.1")
  implementation("androidx.activity:activity-ktx:1.9.0")

  val lifecycleVersion = "2.8.3"
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
  implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

  implementation("androidx.startup:startup-runtime:1.1.1")

  implementation("androidx.datastore:datastore-preferences:1.1.1")

  val navVersion = "2.7.7"
  implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
  implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

  val roomVersion = "2.6.1"
  implementation("androidx.room:room-runtime:$roomVersion")
  implementation("androidx.room:room-ktx:$roomVersion")
  implementation("androidx.room:room-paging:$roomVersion")
  ksp("androidx.room:room-compiler:$roomVersion")

  val pagingVersion = "3.3.0"
  implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")

  val hiltVersion: String by rootProject.extra
  implementation("com.google.dagger:hilt-android:$hiltVersion")
  ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")

  val retrofitVersion = "2.11.0"
  implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
  implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")

  val okhttpVersion = "4.12.0"
  implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
  implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

  val moshiVersion = "1.15.1"
  implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
  ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

  implementation("com.jakewharton.timber:timber:5.0.1")

  debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")

  implementation("com.github.FunkyMuse.KAHelpers:viewbinding:3.2.3")

  testImplementation ("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.2.1")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}