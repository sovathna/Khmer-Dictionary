package io.github.sovathna.khmerdictionary.data

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import okhttp3.OkHttpClient

class OkHttpClientInitializer : Initializer<OkHttpClient> {
  override fun create(context: Context): OkHttpClient {
    return AppInitializer.getInstance(context)
      .initializeComponent(BaseOkHttpClientInitializer::class.java)
  }

  override fun dependencies(): List<Class<out Initializer<*>>> =
    listOf(BaseOkHttpClientInitializer::class.java)
}