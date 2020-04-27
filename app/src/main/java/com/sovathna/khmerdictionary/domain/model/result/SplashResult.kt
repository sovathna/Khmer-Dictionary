package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult

sealed class SplashResult : MviResult {
  data class CheckDatabase(val exists: Boolean) : SplashResult()
}