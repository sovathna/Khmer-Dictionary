package io.github.sovathna.khmerdictionary.data

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class OkHttpClientInitializer : Initializer<OkHttpClient> {
  override fun create(context: Context): OkHttpClient {
    val client = AppInitializer.getInstance(context)
      .initializeComponent(BaseOkHttpClientInitializer::class.java)
    return client.newBuilder()
      .addInterceptor(
        HttpLoggingInterceptor(logger = { Timber.d(it) }).apply {
          level = HttpLoggingInterceptor.Level.HEADERS
        }
      )
      .build()
  }

  override fun dependencies(): List<Class<out Initializer<*>>> =
    listOf(BaseOkHttpClientInitializer::class.java)
}