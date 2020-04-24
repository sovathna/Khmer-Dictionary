package com.sovathna.khmerdictionary.data.remote

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

  @Provides
  @Singleton
  fun baseUrl() = "https://www.example.com/"

  @Provides
  @Singleton
  fun callAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

  @Provides
  @Singleton
  fun converterFactory(): Converter.Factory = MoshiConverterFactory.create().withNullSerialization()

  @Provides
  fun downloadRetrofit(
    baseUrl: String,
    client: OkHttpClient,
    callAdapterFactory: CallAdapter.Factory,
    converterFactory: Converter.Factory
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(client)
      .addCallAdapterFactory(callAdapterFactory)
      .build()

}