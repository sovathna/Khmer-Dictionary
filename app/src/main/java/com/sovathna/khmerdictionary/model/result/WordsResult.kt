package com.sovathna.khmerdictionary.model.result

import com.sovathna.androidmvi.result.MviResult

sealed class WordsResult : MviResult {
  object SelectWordSuccess : WordsResult()
}