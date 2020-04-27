package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult

sealed class DownloadResult : MviResult {
  data class Fail(val throwable: Throwable) : DownloadResult()
  data class Progress(val download: Long, val total: Long) : DownloadResult()
  object Success : DownloadResult()
}