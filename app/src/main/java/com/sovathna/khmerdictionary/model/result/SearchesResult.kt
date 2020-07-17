package com.sovathna.khmerdictionary.model.result

import com.sovathna.androidmvi.result.MviResult

sealed class SearchesResult : MviResult {
  object SelectWordSuccess : SearchesResult()
}