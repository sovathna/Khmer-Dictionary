package com.sovathna.khmerdictionary.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.data.interactor.base.SplashInteractor
import com.sovathna.khmerdictionary.model.intent.SplashIntent
import com.sovathna.khmerdictionary.model.result.SplashResult
import com.sovathna.khmerdictionary.model.state.SplashState
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction

class SplashViewModel @ViewModelInject constructor(
  private val interactor: SplashInteractor
) : MviViewModel<SplashIntent, SplashResult, SplashState>() {

  override val reducer = BiFunction<SplashState, SplashResult, SplashState> { state, result ->
    when (result) {
      is SplashResult.Progressing -> SplashState(
        isInit = false,
        isProgress = true
      )
      is SplashResult.Fail ->
        state.copy(
          error = result.throwable.message ?: "An error has occurred!",
          isProgress = false
        )
      is SplashResult.Downloading ->
        state.copy(
          isProgress = false,
          downloaded = result.downloaded,
          total = result.total
        )
      is SplashResult.Success ->
        state.copy(successEvent = Event(Unit))
    }
  }

  override val stateLiveData: LiveData<SplashState> =
    MutableLiveData<SplashState>().apply {
      intents.compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .scan(SplashState(), reducer)
        .distinctUntilChanged()
        .toFlowable(BackpressureStrategy.BUFFER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(::setValue)
    }
}