package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult

sealed class SplashResult : MviResult {
  object Progress : SplashResult()
  data class Fail(val throwable: Throwable) : SplashResult()
  data class CheckDatabase(val exists: Boolean) : SplashResult()
}