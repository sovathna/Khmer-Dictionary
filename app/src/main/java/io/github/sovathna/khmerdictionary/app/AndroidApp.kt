package io.github.sovathna.khmerdictionary.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.data.AppSettings
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class AndroidApp : Application() {

  @Inject
  lateinit var settings: AppSettings

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    MainScope().launch {
      settings.setNightMode()
    }

  }


}