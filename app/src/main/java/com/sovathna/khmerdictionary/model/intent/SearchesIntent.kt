package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class SearchesIntent : MviIntent {
  data class GetWords(
    val searchTerm: String
  ) : SearchesIntent()
}