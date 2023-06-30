package io.github.sovathna.khmerdictionary.initializer

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.data.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DownloadServiceInitializer : Initializer<ApiService> {
    override fun create(context: Context): ApiService {
        TODO("Not yet implemented")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(DownloadRetrofitInitializer::class.java)
}

class DownloadRetrofitInitializer : Initializer<Retrofit> {
    override fun create(context: Context): Retrofit {
        val client = AppInitializer.getInstance(context)
            .initializeComponent(DownloadClientInitializer::class.java)
        return Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .build()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(DownloadClientInitializer::class.java)
}

class DownloadClientInitializer : Initializer<OkHttpClient> {
    override fun create(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
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
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}