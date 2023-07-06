package io.github.sovathna.khmerdictionary.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(private val store: DataStore<Preferences>) {

  companion object {
    private val KEY_DB_VERSION = intPreferencesKey("db_version")
    private val KEY_DETAIL_ID = longPreferencesKey("detail_id")
    private val KEY_FONT_SIZE = intPreferencesKey("font_size")
  }

  val detailIdFlow: Flow<Long> = store.data.map { it[KEY_DETAIL_ID] ?: 0L }

  suspend fun setDetailId(id: Long) = store.edit { it[KEY_DETAIL_ID] = id }

  suspend fun getDbVersion(): Int = store.data.map { it[KEY_DB_VERSION] ?: 0 }.first()


  suspend fun setDbVersion(version: Int = 1) = store.edit { it[KEY_DB_VERSION] = version }

  suspend fun getFontSize(): Int = store.data.map { it[KEY_FONT_SIZE] ?: 16 }.first()
  suspend fun setFontSize(fontSize: Int) = store.edit { it[KEY_FONT_SIZE] = fontSize }

}