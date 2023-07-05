package io.github.sovathna.khmerdictionary.module

import androidx.startup.AppInitializer
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.sovathna.khmerdictionary.data.ApiService
import io.github.sovathna.khmerdictionary.data.DownloadService
import io.github.sovathna.khmerdictionary.initializer.ApiServiceInitializer
import io.github.sovathna.khmerdictionary.initializer.DownloadServiceInitializer
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object SplashServiceModule {

  @Provides
  @ViewModelScoped
  fun downloadService(initializer: AppInitializer): DownloadService =
    initializer.initializeComponent(DownloadServiceInitializer::class.java)

  @Provides
  @ViewModelScoped
  fun apiService(initializer: AppInitializer): ApiService =
    initializer.initializeComponent(ApiServiceInitializer::class.java)

  @Provides
  @ViewModelScoped
  fun moshi(): Moshi = Moshi.Builder().build()

  @Provides
  @ViewModelScoped
  fun converter(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)
}