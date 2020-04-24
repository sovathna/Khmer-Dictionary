package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import com.sovathna.khmerdictionary.domain.model.result.DownloadResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class DownloadInteractor : MviInteractor<DownloadIntent, DownloadResult>() {

  abstract val download: ObservableTransformer<DownloadIntent.Download, DownloadResult>

  abstract val progress: ObservableTransformer<DownloadIntent.Progress, DownloadResult>

  override val intentsProcessor = ObservableTransformer<DownloadIntent, DownloadResult> {
    it.publish { intent ->
      Observable.merge(
        intent.ofType(DownloadIntent.Download::class.java).compose(download),
        intent.ofType(DownloadIntent.Progress::class.java).compose(progress)
      )
    }
  }
}