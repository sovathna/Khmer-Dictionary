package io.github.sovathna.khmerdictionary.ui.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.data.AppSettings
import io.github.sovathna.khmerdictionary.ui.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val settings: AppSettings
) : BaseViewModel<SettingsState>(SettingsState()) {

  init {
    viewModelScope.launch {
      settings.fontSizeFlow
        .distinctUntilChanged()
        .collectLatest {
          setState(current.copy(fontSize = it))
        }
    }

    viewModelScope.launch {
      settings.nightModeFlow
        .distinctUntilChanged()
        .collectLatest {
          settings.setNightMode()
          setState(current.copy(nightMode = if (it == -1) 0 else it))
        }
    }
  }

  fun setFontSize(fontSize: Float) {
    viewModelScope.launch {
      settings.setFontSize(fontSize)
    }
  }

  fun setNightMode(nightMode: Int) {
    viewModelScope.launch {
      settings.setNightMode(nightMode)
    }
  }

}