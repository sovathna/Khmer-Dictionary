package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class HistoriesIntent : MviIntent {
  data class GetWords(
    val offset: Int,
    val pageSize: Int
  ) : HistoriesIntent()

  object ClearHistories : HistoriesIntent()
}