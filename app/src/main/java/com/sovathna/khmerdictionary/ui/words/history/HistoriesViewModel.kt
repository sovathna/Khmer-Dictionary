package com.sovathna.khmerdictionary.ui.words.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.livedata.Event
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
      when (result) {
        is HistoriesResult.Success ->
          state.copy(
            isInit = false,
            wordsLiveData = result.historiesPager.liveData
              .map { it.map { it.toWordItem() } }
              .cachedIn(viewModelScope),
            loadSuccess = Event(Unit)
          )
        is HistoriesResult.SelectWordSuccess -> state
        is HistoriesResult.ClearHistoriesSuccess -> state
      }
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