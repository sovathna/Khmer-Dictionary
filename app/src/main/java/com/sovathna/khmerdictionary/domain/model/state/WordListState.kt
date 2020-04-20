package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.domain.model.WordList

data class WordListState(
    val isInit: Boolean = false,
    val isProgress: Boolean = false,
    val error: String? = null,
    val wordList: WordList? = null
) : MviState