package io.github.sovathna.khmerdictionary.module

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.sovathna.khmerdictionary.data.ApiService
import io.github.sovathna.khmerdictionary.data.DownloadService
import io.github.sovathna.khmerdictionary.di.qualifier.Api
import io.github.sovathna.khmerdictionary.di.qualifier.Download
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(ViewModelComponent::class)
object SplashServiceModule {

    @Provides
    @ViewModelScoped
    fun downloadService(@Download retrofit: Retrofit): DownloadService =
        retrofit.create()

    @Provides
    @ViewModelScoped
    fun apiService(@Api retrofit: Retrofit): ApiService =
        retrofit.create()

    @Provides
    @ViewModelScoped
    fun moshi(): Moshi = Moshi.Builder().build()

    @Provides
    @ViewModelScoped
    fun converter(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)
}