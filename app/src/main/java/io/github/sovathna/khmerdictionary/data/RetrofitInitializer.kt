package io.github.sovathna.khmerdictionary.data

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import retrofit2.Retrofit

class RetrofitInitializer : Initializer<Retrofit> {
    override fun create(context: Context): Retrofit {
        val client = AppInitializer.getInstance(context)
            .initializeComponent(OkHttpClientInitializer::class.java)
        return Retrofit.Builder()
            .baseUrl("https://sovathna.github.io/")
            .client(client)
            .build()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(OkHttpClientInitializer::class.java)
}