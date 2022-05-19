package io.github.sovathna.khmerdictionary.ui.settings

import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.model.Settings

data class SettingsState(val settings: Settings = Settings(), val changed: Event<Unit>? = null)