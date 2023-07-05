package io.github.sovathna.khmerdictionary.initializer

import android.content.Context
import androidx.startup.Initializer
import com.squareup.moshi.Moshi

class MoshiInitializer : Initializer<Moshi> {
  override fun create(context: Context): Moshi {
    return Moshi.Builder().build()
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}