package com.sovathna.khmerdictionary.app

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<DictApp> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<DictApp>
}