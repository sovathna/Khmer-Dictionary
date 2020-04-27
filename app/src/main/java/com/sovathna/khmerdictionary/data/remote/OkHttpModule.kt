package com.sovathna.khmerdictionary.data.remote

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class OkHttpModule {

  @Provides
  fun downloadOkHttp() = OkHttpClient()
    .newBuilder()
    .build()

}