package io.github.sovathna.khmerdictionary.model

import androidx.appcompat.app.AppCompatDelegate

data class Settings(val themeMode: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
  val position = when (themeMode) {
    AppCompatDelegate.MODE_NIGHT_NO -> 0
    AppCompatDelegate.MODE_NIGHT_YES -> 1
    else -> 2

  }
}