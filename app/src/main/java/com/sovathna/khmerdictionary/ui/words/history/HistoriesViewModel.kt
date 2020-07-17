package com.sovathna.khmerdictionary.ui.words.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.data.interactor.base.HistoriesInteractor
import com.sovathna.khmerdictionary.model.result.HistoriesResult
import com.sovathna.khmerdictionary.model.state.HistoriesState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class HistoriesViewModel @ViewModelInject constructor(
  private val interactor: HistoriesInteractor
) : MviViewModel<MviIntent, HistoriesResult, HistoriesState>() {

  override val reducer =
    BiFunction<HistoriesState, HistoriesResult, HistoriesState> { state, result ->
      state
    }

  override val stateLiveData: LiveData<HistoriesState> =
    MutableLiveData<HistoriesState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(HistoriesState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}