package io.github.sovathna.khmerdictionary.initializer

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.data.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.File

class ApiServiceInitializer : Initializer<ApiService> {
  override fun create(context: Context): ApiService {
    val retrofit = AppInitializer.getInstance(context)
      .initializeComponent(ApiRetrofitInitializer::class.java)
    return retrofit.create(ApiService::class.java)
  }

  override fun dependencies(): List<Class<out Initializer<*>>> =
    listOf(ApiRetrofitInitializer::class.java)

}

class ApiRetrofitInitializer : Initializer<Retrofit> {
  override fun create(context: Context): Retrofit {
    val client = AppInitializer.getInstance(context)
      .initializeComponent(ApiClientInitializer::class.java)
    val moshi = AppInitializer.getInstance(context)
      .initializeComponent(MoshiInitializer::class.java)
    return Retrofit.Builder()
      .baseUrl("https://example.com/")
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(client)
      .build()
  }

  override fun dependencies(): List<Class<out Initializer<*>>> =
    listOf(ApiClientInitializer::class.java, MoshiInitializer::class.java)
}

class ApiClientInitializer : Initializer<OkHttpClient> {
  override fun create(context: Context): OkHttpClient {
    val cache = Cache(File(context.cacheDir, "okhttp_api_cache"), 5 * 1024 * 1024L)
    return OkHttpClient.Builder()
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
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}