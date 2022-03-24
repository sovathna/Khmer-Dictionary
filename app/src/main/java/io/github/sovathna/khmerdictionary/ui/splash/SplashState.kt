package io.github.sovathna.khmerdictionary.ui.splash

import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.toKmStringNum

data class SplashState(
    val read: Double = 0.0,
    val size: Double = 0.0,
    val error: Throwable? = null,
    val redirectEvent: Event<Unit>? =null
) {
    val isStarting = size == 0.0
    val isDownloading = size > 0.0 && read < size
    val isDone = size > 0.0 && read == size

    val readString = String.format("%.02f", read).toKmStringNum()
    val sizeString = String.format("%.02f", size).toKmStringNum()
}
