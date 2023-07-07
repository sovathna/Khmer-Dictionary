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
  }

  fun setFontSize(fontSize: Float) {
    viewModelScope.launch {
      settings.setFontSize(fontSize)
    }
  }
}