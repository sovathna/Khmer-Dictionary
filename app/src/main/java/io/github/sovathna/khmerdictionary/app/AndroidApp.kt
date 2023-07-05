package io.github.sovathna.khmerdictionary.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.sovathna.khmerdictionary.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class AndroidApp : Application() {

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      TODO("implement Timber release tree")
    }
  }

}