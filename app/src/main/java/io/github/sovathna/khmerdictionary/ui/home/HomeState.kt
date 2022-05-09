package io.github.sovathna.khmerdictionary.ui.home

import androidx.annotation.Keep
import androidx.paging.PagingData
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.model.WordEntity

data class HomeState(
  val shouldScrollTop:Event<Unit>?=null,
  val paging: PagingData<WordEntity>? = null
)

@Keep
enum class HomeType {
  ALL,
  FAVORITE,
  HISTORY
}