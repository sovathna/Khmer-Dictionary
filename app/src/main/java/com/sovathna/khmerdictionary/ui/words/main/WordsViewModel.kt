package com.sovathna.khmerdictionary.ui.words.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.data.interactor.WordsInteractorImpl
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import com.sovathna.khmerdictionary.model.state.WordsState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class WordsViewModel @ViewModelInject constructor(
  private val interactor: WordsInteractorImpl
) : MviViewModel<WordsIntent, WordsResult, WordsState>() {

  val wordsLiveData = interactor.getWords()
    .liveData
    .map { it.map { it.toWordItem() } }
    .cachedIn(viewModelScope)

  override val reducer =
    BiFunction<WordsState, WordsResult, WordsState> { state, _ ->
      state
    }

  override val stateLiveData: LiveData<WordsState> =
    MutableLiveData<WordsState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(WordsState, reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}