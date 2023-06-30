package io.github.sovathna.khmerdictionary.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.di.qualifier.Api
import io.github.sovathna.khmerdictionary.di.qualifier.Download
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ViewModelComponent::class)
object OkHttpModule {

    @Provides
    @ViewModelScoped
    @Download
    fun downloadClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(0L, TimeUnit.MILLISECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor(logger = { Timber.d(it) })
                        .apply { level = HttpLoggingInterceptor.Level.BASIC }
                )
            }
        }
        .build()

    @Provides
    @ViewModelScoped
    @Api
    fun apiClient(@Api cache: Cache): OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor(logger = { Timber.d(it) })
                        .apply { level = HttpLoggingInterceptor.Level.BODY }
                )
            }
        }
        .build()

    @Provides
    @ViewModelScoped
    @Api
    fun apiCache(@ApplicationContext context: Context): Cache =
        Cache(File(context.cacheDir, "okhttp_api_cache"), 5 * 1024 * 1024L)
}