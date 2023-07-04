package io.github.sovathna.khmerdictionary.ui.words

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class WordsViewModel @Inject constructor() : BaseViewModel<WordsState>(WordsState()) {

    init {
        getWords()
    }

    fun getWords() {

    }
}