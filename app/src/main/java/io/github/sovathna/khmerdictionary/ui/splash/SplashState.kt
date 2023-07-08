package io.github.sovathna.khmerdictionary.ui.splash

import io.github.sovathna.khmerdictionary.ui.Event

data class SplashState(
  val type: Type = Type.GET_CONFIG,
  val size: Double = 0.0,
  val read: Double = 0.0,
  val doneEvent: Event<Unit>? = null,
  val error: String? = null,
) {

  enum class Type {
    GET_CONFIG,
    DOWNLOADING,
    EXTRACTING
  }

  val isNotError = error.isNullOrBlank()
  val isError = !isNotError
  val isDeterminate = size == 0.0 && size == read || type != Type.DOWNLOADING
}
