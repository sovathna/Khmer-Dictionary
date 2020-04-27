package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import com.sovathna.khmerdictionary.domain.model.result.DownloadResult
import io.reactivex.ObservableTransformer

abstract class DownloadInteractor : MviInteractor<DownloadIntent, DownloadResult>() {

  abstract val download: ObservableTransformer<DownloadIntent.Download, DownloadResult>


  override val intentsProcessor = ObservableTransformer<DownloadIntent, DownloadResult> {
    it.publish { intent ->
      intent.ofType(DownloadIntent.Download::class.java).compose(download)
    }
  }
}