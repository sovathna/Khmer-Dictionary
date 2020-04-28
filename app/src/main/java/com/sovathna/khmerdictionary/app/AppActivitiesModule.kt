package com.sovathna.khmerdictionary.app

import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.ui.main.MainFragmentsModule
import com.sovathna.khmerdictionary.ui.splash.SplashActivity
import com.sovathna.khmerdictionary.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppActivitiesModule {

  @ContributesAndroidInjector(modules = [SplashModule::class])
  abstract fun splashActivity(): SplashActivity

  @ContributesAndroidInjector(modules = [MainFragmentsModule::class])
  abstract fun mainActivity(): MainActivity

}