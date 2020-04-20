package com.sovathna.khmerdictionary.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class DictApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
    }
}