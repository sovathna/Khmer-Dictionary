package io.github.sovathna.khmerdictionary.ui.home

import androidx.annotation.Keep
import androidx.paging.PagingData
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.model.WordEntity

data class HomeState(
  val scrollToTopEvent: Event<Unit>? = null,
  val paging: PagingData<WordEntity>? = null,
  val searchTerm: String? = null
)

@Keep
enum class HomeType {
  ALL,
  FAVORITE,
  HISTORY
}