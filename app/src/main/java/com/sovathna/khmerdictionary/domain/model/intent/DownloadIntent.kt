package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class DownloadIntent : MviIntent {

  object Download : DownloadIntent()

  data class Progress(val download: Long, val total: Long) : DownloadIntent()

}