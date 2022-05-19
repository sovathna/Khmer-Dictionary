package io.github.sovathna.khmerdictionary.ui.main

import io.github.sovathna.khmerdictionary.ui.detail.DetailState

data class MainState(
  val detail: DetailState? = null,
  val isObserved: Boolean = false
)
