package com.sovathna.khmerdictionary.app

import android.content.Context
import com.sovathna.khmerdictionary.data.DataModule
import com.sovathna.khmerdictionary.vm.ViewModelsModule
import com.sovathna.khmerdictionary.vmfactory.ViewModelFactoryModule
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
  includes = [
    AppActivitiesModule::class,
    DataModule::class,
    ViewModelsModule::class,
    ViewModelFactoryModule::class
  ]
)
abstract class AppModule {
  @Binds
  @Singleton
  abstract fun context(app: AndroidApp): Context
}