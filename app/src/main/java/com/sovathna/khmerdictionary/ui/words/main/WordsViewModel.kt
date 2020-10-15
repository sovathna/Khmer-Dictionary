package com.sovathna.khmerdictionary.ui.words.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import androidx.paging.map
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.data.interactor.base.WordsInteractor
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import com.sovathna.khmerdictionary.model.state.WordsState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class WordsViewModel @ViewModelInject constructor(
  private val interactor: WordsInteractor
) : MviViewModel<WordsIntent, WordsResult, WordsState>() {

  override val reducer =
    BiFunction<WordsState, WordsResult, WordsState> { state, result ->
      when (result) {
        is WordsResult.SelectWordSuccess ->
          state
        is WordsResult.PagingSuccess ->
          state.copy(
            isInit = false,
            wordsLiveData = result.wordsPager.liveData
              .map { it.map { it.toWordItem() } }
              .cachedIn(viewModelScope)
          )
      }
    }

  override val stateLiveData: LiveData<WordsState> =
    MutableLiveData<WordsState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(WordsState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}