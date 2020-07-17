package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class HistoriesIntent : MviIntent {
  object ClearHistories : HistoriesIntent()
}