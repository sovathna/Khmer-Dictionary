package com.sovathna.khmerdictionary.ui.download

import com.sovathna.androidmvi.fragment.MviDialogFragment
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import com.sovathna.khmerdictionary.domain.model.state.DownloadState
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_download.*
import javax.inject.Inject

class DownloadFragment : MviDialogFragment<DownloadIntent, DownloadState, DownloadViewModel>(
  R.layout.fragment_download
) {

  @Inject
  lateinit var downloadIntent: PublishSubject<DownloadIntent.Download>

  @Inject
  lateinit var progressIntent: PublishSubject<DownloadIntent.Progress>

  override fun intents(): Observable<DownloadIntent> =
    Observable.merge(downloadIntent, progressIntent)

  override fun render(state: DownloadState) {
    LogUtil.i("state: $state")
    with(state) {
      if (isInit) downloadIntent.onNext(DownloadIntent.Download)

      pb.isIndeterminate = isProgress

      pb.max = total.toInt()
      pb.progress = download.toInt()

      successEvent

    }
  }
}