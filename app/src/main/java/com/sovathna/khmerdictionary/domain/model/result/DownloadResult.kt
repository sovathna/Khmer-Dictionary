package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult
import java.io.File

sealed class DownloadResult : MviResult {
  data class Fail(val throwable: Throwable) : DownloadResult()
  data class Progress(val download: Long, val total: Long, val isExtract: Boolean = false) : DownloadResult()
  object Success : DownloadResult()
}