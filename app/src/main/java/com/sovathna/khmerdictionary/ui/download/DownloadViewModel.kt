package com.sovathna.khmerdictionary.ui.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.DownloadInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import com.sovathna.khmerdictionary.domain.model.result.DownloadResult
import com.sovathna.khmerdictionary.domain.model.state.DownloadState
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction

class DownloadViewModel(
  private val interactor: DownloadInteractor
) : MviViewModel<DownloadIntent, DownloadResult, DownloadState>() {

  override val reducer = BiFunction<DownloadState, DownloadResult, DownloadState> { state, result ->
    when (result) {
      is DownloadResult.Fail ->
        state.copy(
          error = result.throwable.message ?: "An error has occurred!"
        )
      is DownloadResult.Progress ->
        state.copy(
          isInit = false,
          download = result.download,
          total = result.total
        )
      is DownloadResult.Success ->
        state.copy(successEvent = Event(Unit))
    }
  }

  override val stateLiveData: LiveData<DownloadState> =
    MutableLiveData<DownloadState>().apply {
      intents.compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .scan(DownloadState(), reducer)
        .distinctUntilChanged()
        .toFlowable(BackpressureStrategy.BUFFER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(::setValue)
    }
}