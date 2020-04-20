package com.sovathna.khmerdictionary.app

import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.ui.main.MainFragmentsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppActivitiesModule {

  @ContributesAndroidInjector(modules = [MainFragmentsModule::class])
  abstract fun mainActivity(): MainActivity

}