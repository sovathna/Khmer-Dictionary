package com.sovathna.khmerdictionary.vmfactory

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @Singleton
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}