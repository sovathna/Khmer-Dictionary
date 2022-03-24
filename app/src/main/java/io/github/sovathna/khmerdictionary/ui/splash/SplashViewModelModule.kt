package io.github.sovathna.khmerdictionary.ui.splash

import androidx.startup.AppInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.sovathna.khmerdictionary.data.SplashServiceInitializer
import io.github.sovathna.khmerdictionary.domain.SplashService

@Module
@InstallIn(ViewModelComponent::class)
object SplashViewModelModule {

    @Provides
    @ViewModelScoped
    fun splashService(initializer: AppInitializer): SplashService =
        initializer.initializeComponent(SplashServiceInitializer::class.java)

}