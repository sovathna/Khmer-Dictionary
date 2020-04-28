package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.ui.wordlist.WordItem

data class WordListState(
  val isInit: Boolean = true,
  val isMore: Boolean = false,
  val last: Int? = null,
  val words: List<WordItem>? = null
) : MviState