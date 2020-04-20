package com.sovathna.khmerdictionary.app

import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppActivitiesModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

}