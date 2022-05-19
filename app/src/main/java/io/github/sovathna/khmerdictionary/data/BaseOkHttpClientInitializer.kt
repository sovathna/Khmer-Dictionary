package io.github.sovathna.khmerdictionary.data

import android.content.Context
import androidx.startup.Initializer
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class BaseOkHttpClientInitializer : Initializer<OkHttpClient> {
  override fun create(context: Context): OkHttpClient =
    OkHttpClient.Builder()
      .readTimeout(Integer.MAX_VALUE.toLong(), TimeUnit.MILLISECONDS)
      .build()

  override fun dependencies(): List<Class<out Initializer<*>>> = listOf()
}