package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.domain.model.WordList

data class WordListState(
  val isInit: Boolean = true,
  val isProgress: Boolean = false,
  val error: String? = null,
  val wordList: WordList? = null
) : MviState