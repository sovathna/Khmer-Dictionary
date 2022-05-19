package io.github.sovathna.khmerdictionary.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.data.SettingsDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class AndroidApp : Application() {

  @Inject
  lateinit var settings: SettingsDataSource

  override fun onCreate() {
    super.onCreate()
    val themeMode = runBlocking { settings.settingsFlow.map { it.themeMode }.first() }
    if (AppCompatDelegate.getDefaultNightMode() != themeMode) {
      AppCompatDelegate.setDefaultNightMode(themeMode)
    }
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    } else {
      Timber.plant(object : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
          TODO("Not yet implemented")
        }
      })
    }
  }

}