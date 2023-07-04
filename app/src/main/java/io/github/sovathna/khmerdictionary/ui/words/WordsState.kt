package io.github.sovathna.khmerdictionary.ui.words

import io.github.sovathna.khmerdictionary.model.ui.WordUi

data class WordsState(
    val isLoading: Boolean = false,
    val words: List<WordUi>? = null,
    val page: Int = 1
)
