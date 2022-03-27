package io.github.sovathna.khmerdictionary.ui.home

import androidx.paging.PagingData
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.model.WordEntity

data class HomeState(
  val searchTerm: Event<String>? = null,
  val paging: PagingData<WordEntity>? = null
)