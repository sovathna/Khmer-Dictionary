package io.github.sovathna.khmerdictionary.ui.words

import androidx.paging.PagingData
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.model.WordEntity

data class WordsState(
  val scrollToTopEvent: Event<Unit>? = null,
  val paging: PagingData<WordEntity>? = null,
  val searchTerm: String? = null,
  val isEmpty: Boolean? = null
)