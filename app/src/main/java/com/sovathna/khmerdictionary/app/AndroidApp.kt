package com.sovathna.khmerdictionary.app

import android.app.Application
import com.sovathna.androidmvi.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidApp : Application() {

  override fun onCreate() {
    super.onCreate()
    Logger.init()
  }

}