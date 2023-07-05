package io.github.sovathna.khmerdictionary.initializer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.module.store

class StoreInitializer : Initializer<DataStore<Preferences>> {
  override fun create(context: Context): DataStore<Preferences> {
    return context.store
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}