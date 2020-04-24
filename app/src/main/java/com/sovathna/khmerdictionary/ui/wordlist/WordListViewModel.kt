package com.sovathna.khmerdictionary.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction

class WordListViewModel(
  private val interactor: WordListInteractor
) : MviViewModel<WordListIntent, WordListResult, WordListState>() {

  override val reducer =
    BiFunction<WordListState, WordListResult, WordListState> { state, result ->
      when (result) {
        is WordListResult.Success -> state.copy(wordList = result.wordList)
      }
    }

  override val stateLiveData: LiveData<WordListState> =
    MutableLiveData<WordListState>().apply {
      val disposable = intents.compose(interactor.intentsProcessor)
        .scan(WordListState(), reducer)
        .distinctUntilChanged()
        .toFlowable(BackpressureStrategy.BUFFER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(::setValue)
      disposables.add(disposable)
    }
}