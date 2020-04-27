package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult

sealed class SplashResult : MviResult {
  object Progressing : SplashResult()
  data class Downloading(val downloaded: Long, val total: Long) : SplashResult()
  data class Fail(val throwable: Throwable) : SplashResult()
  object Success : SplashResult()
}