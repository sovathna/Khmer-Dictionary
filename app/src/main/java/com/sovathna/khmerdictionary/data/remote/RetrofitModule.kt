package com.sovathna.khmerdictionary.data.remote

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

  @Provides
  @Singleton
  fun baseUrl(): String =
    "https://www.example.com/"

  @Provides
  @Singleton
  fun callAdapterFactory(): CallAdapter.Factory =
    RxJava2CallAdapterFactory.create()

  @Provides
  @Singleton
  fun moshi(): Moshi = Moshi.Builder().build()

  @Provides
  @Singleton
  fun converterFactory(moshi: Moshi): Converter.Factory =
    MoshiConverterFactory
      .create(moshi)
      .withNullSerialization()

  @Provides
  @Singleton
  fun downloadRetrofit(
    baseUrl: String,
    client: OkHttpClient,
    callAdapterFactory: CallAdapter.Factory
  ): Retrofit =
    Retrofit
      .Builder()
      .baseUrl(baseUrl)
      .client(client)
      .addCallAdapterFactory(callAdapterFactory)
      .build()

}