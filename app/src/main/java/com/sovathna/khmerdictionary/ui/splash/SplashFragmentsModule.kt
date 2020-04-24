package com.sovathna.khmerdictionary.ui.splash

import com.sovathna.khmerdictionary.ui.download.DownloadModule
import com.sovathna.khmerdictionary.ui.download.DownloadFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashFragmentsModule {

  @ContributesAndroidInjector(modules = [DownloadModule::class])
  abstract fun downloadFragment(): DownloadFragment

}