package io.github.sovathna.khmerdictionary.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(private val store: DataStore<Preferences>) {

    companion object {
        private val KEY_DB_VERSION = intPreferencesKey("db_version")
    }

    suspend fun getDbVersion(): Int = store.data.map { it[KEY_DB_VERSION] ?: 0 }.first()


    suspend fun setDbVersion(version: Int = 1) = store.edit { it[KEY_DB_VERSION] = version }

}