package io.github.sovathna.khmerdictionary.ui.words

import io.github.sovathna.khmerdictionary.model.ui.WordUi

data class WordsState(
    val isLoading: Boolean = false,
    val words: List<WordUi>? = null,
    val page: Int = 1,
    val isMore: Boolean = true,
    val type: WordsType = WordsType.Words
) {
    val isEmpty = words?.isEmpty() == true
    val isSearchAndNull = type is WordsType.Searches && words == null
}
