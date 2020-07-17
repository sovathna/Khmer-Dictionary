package com.sovathna.khmerdictionary.model.result

import com.sovathna.androidmvi.result.MviResult

sealed class HistoriesResult : MviResult {
  object SelectWordSuccess : HistoriesResult()

  object ClearHistoriesSuccess : HistoriesResult()
}