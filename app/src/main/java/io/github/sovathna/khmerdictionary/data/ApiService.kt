package io.github.sovathna.khmerdictionary.data

import io.github.sovathna.khmerdictionary.model.Config
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

  @GET
  suspend fun getConfig(@Url url: String): Config

}