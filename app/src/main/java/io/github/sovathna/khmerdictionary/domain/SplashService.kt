package io.github.sovathna.khmerdictionary.domain

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface SplashService {

  @Streaming
  @GET
  suspend fun downloadDatabase(@Url url: String): ResponseBody

}