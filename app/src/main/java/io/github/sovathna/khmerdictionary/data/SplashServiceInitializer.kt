package io.github.sovathna.khmerdictionary.data

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.domain.SplashService

class SplashServiceInitializer : Initializer<SplashService> {
    override fun create(context: Context): SplashService {
        val retrofit = AppInitializer.getInstance(context)
            .initializeComponent(RetrofitInitializer::class.java)
        return retrofit.create(SplashService::class.java)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(RetrofitInitializer::class.java)
}