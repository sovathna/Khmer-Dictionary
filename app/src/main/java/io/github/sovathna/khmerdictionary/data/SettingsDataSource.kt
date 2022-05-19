package io.github.sovathna.khmerdictionary.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.github.sovathna.khmerdictionary.model.Settings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataSource @Inject constructor(private val settingsStore: DataStore<Preferences>) {

  companion object {
    private val KEY_THEME_MODE = intPreferencesKey("theme_mode")
    private val KEY_DOWNLOADED = booleanPreferencesKey("is_downloaded")
  }

  val settingsFlow = settingsStore.data.map {
    Settings(
      themeMode = it[KEY_THEME_MODE] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )
  }

  suspend fun updateSettings(settings: Settings) {
    settingsStore.edit {
      it[KEY_THEME_MODE] = settings.themeMode
    }
  }

  suspend fun isDownloaded(): Boolean =
    settingsStore.data.map { it[KEY_DOWNLOADED] ?: false }.first()

  suspend fun downloaded() {
    settingsStore.edit {
      it[KEY_DOWNLOADED] = true
    }
  }

}