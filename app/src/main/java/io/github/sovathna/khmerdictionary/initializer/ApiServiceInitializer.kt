package io.github.sovathna.khmerdictionary.initializer

import android.content.Context
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.data.ApiService

class ApiServiceInitializer : Initializer<ApiService> {
    override fun create(context: Context): ApiService {
        TODO("Not yet implemented")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        TODO("Not yet implemented")
    }
}