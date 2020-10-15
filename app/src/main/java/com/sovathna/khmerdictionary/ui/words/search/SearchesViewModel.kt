package com.sovathna.khmerdictionary.ui.words.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import androidx.paging.map
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.data.interactor.base.SearchesInteractor
import com.sovathna.khmerdictionary.model.result.SearchesResult
import com.sovathna.khmerdictionary.model.state.SearchWordsState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class SearchesViewModel @ViewModelInject constructor(
  private val interactor: SearchesInteractor
) : MviViewModel<MviIntent, SearchesResult, SearchWordsState>() {

  override val reducer =
    BiFunction<SearchWordsState, SearchesResult, SearchWordsState> { state, result ->

      when (result) {
        is SearchesResult.Success ->
          state.copy(
            isInit = false,
            wordsLiveData = result.searchesPager.liveData
              .map { it.map { it.toWordItem() } }
              .cachedIn(viewModelScope),
            loadSuccess = Event(Unit)
          )
        is SearchesResult.SelectWordSuccess -> state
      }
    }

  override val stateLiveData: LiveData<SearchWordsState> =
    MutableLiveData<SearchWordsState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(SearchWordsState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }
}