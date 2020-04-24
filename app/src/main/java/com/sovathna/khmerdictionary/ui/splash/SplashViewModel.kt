package com.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.result.SplashResult
import com.sovathna.khmerdictionary.domain.model.state.SplashState
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction

class SplashViewModel(
  private val interactor: SplashInteractor
) : MviViewModel<SplashIntent, SplashResult, SplashState>() {

  override val reducer = BiFunction<SplashState, SplashResult, SplashState> { state, result ->
    when (result) {
      is SplashResult.CheckDatabase -> state.copy(isInit = false, exists = Event(result.exists))
      is SplashResult.Progress -> TODO()
      is SplashResult.Fail -> TODO()
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