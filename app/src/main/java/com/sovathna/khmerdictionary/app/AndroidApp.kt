package com.sovathna.khmerdictionary.app

import com.sovathna.khmerdictionary.BuildConfig
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class AndroidApp : DaggerApplication() {

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.factory().create(this)
  }

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG)
      Timber.plant(Timber.DebugTree())
  }

}