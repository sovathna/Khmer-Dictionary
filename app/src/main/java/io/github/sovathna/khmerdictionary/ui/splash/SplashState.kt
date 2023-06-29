package io.github.sovathna.khmerdictionary.ui.splash

import io.github.sovathna.khmerdictionary.ui.Event

data class SplashState(
    val isDownloading: Boolean? = null,
    val size: Double = 0.0,
    val read: Double = 0.0,
    val doneEvent: Event<Unit>? = null,
    val error: String? = null,
) {
    val isNotError = error.isNullOrBlank()
    val isError = !isNotError
    val isDeterminate = size == 0.0 && size == read
}
