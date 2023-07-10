package io.github.sovathna.khmerdictionary.data

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(
  @ApplicationContext private val context: Context,
  private val store: DataStore<Preferences>
) {

  companion object {
    private val KEY_DB_VERSION = intPreferencesKey("db_version")
    private val KEY_DETAIL_ID = longPreferencesKey("detail_id")
    private val KEY_FONT_SIZE = floatPreferencesKey("font_size")
    private val KEY_NIGHT_MODE = intPreferencesKey("night_mode")
    private val KEY_IS_NOTIFICATION = floatPreferencesKey("is_notification")
  }

  val detailIdFlow: Flow<Long> = store.data.map { it[KEY_DETAIL_ID] ?: 0L }

  suspend fun setDetailId(id: Long) = store.edit { it[KEY_DETAIL_ID] = id }

  suspend fun getDbVersion(): Int = store.data.map { it[KEY_DB_VERSION] ?: 0 }.first()


  suspend fun setDbVersion(version: Int = 1) = store.edit { it[KEY_DB_VERSION] = version }

  val fontSizeFlow: Flow<Float> = store.data.map { it[KEY_FONT_SIZE] ?: 16.0f }
  suspend fun getFontSize(): Float = store.data.map { it[KEY_FONT_SIZE] ?: 16.0f }.first()
  suspend fun setFontSize(fontSize: Float) = store.edit { it[KEY_FONT_SIZE] = fontSize }

  val nightModeFlow: Flow<Int> =
    store.data.map { it[KEY_NIGHT_MODE] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM }

  suspend fun getNightMode(): Int = nightModeFlow.first()

  suspend fun setNightMode(nightMode: Int) {
    store.edit { it[KEY_NIGHT_MODE] = nightMode }
    setNightMode()
  }

  suspend fun setNightMode() {
    val nightMode = getNightMode()
    val systemNightMode = getSystemNightMode()
    Timber.tag("debug").d("$nightMode $systemNightMode")
    if (nightMode != systemNightMode) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val uiManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        uiManager?.setApplicationNightMode(if (nightMode == -1) UiModeManager.MODE_NIGHT_AUTO else nightMode)
      } else {
        AppCompatDelegate.setDefaultNightMode(nightMode)
      }
    }
  }

  fun getSystemNightMode(): Int {
    val flags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return when (flags) {
      Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_NO
      else -> AppCompatDelegate.MODE_NIGHT_YES
    }
  }

}