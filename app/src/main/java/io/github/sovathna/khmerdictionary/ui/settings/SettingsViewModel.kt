package io.github.sovathna.khmerdictionary.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.data.SettingsDataSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val dataSource: SettingsDataSource) :
  BaseViewModel<SettingsState>(SettingsState()) {

  init {
    collectSettings()
  }

  private fun collectSettings() {
    viewModelScope.launch {
      dataSource.settingsFlow
        .distinctUntilChanged()
        .collectLatest {
          setState(current.copy(settings = it))
        }
    }
  }

  fun selectTheme(position: Int) {
    val settings = current.settings
    viewModelScope.launch {
      val themeMode =
        when (position) {
          0 -> AppCompatDelegate.MODE_NIGHT_NO
          1 -> AppCompatDelegate.MODE_NIGHT_YES
          else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        }
      if (themeMode != settings.themeMode) {
        dataSource.updateSettings(settings.copy(themeMode = themeMode))
        setState(current.copy(changed = Event(Unit)))
      }
    }
  }

}