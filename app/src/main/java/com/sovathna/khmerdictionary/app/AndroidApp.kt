package com.sovathna.khmerdictionary.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class AndroidApp : DaggerApplication() {

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.factory().create(this)
  }

}