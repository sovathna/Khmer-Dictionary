package com.sovathna.khmerdictionary.ui.wordlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class WordListViewModel(
  private val interactor: WordListInteractor
) : MviViewModel<WordListIntent, WordListResult, WordListState>() {

  override val reducer =
    BiFunction<WordListState, WordListResult, WordListState> { state, result ->
      when (result) {
        is WordListResult.Success -> state.copy(
          isInit = false,
          words = if (result.isMore) state.words?.toMutableList()
            ?.apply { addAll(result.words.map { WordItem(it) }) }
          else result.words.map { WordItem(it) },
          isMore = result.words.size == Const.PAGE_SIZE
        )
        is WordListResult.Select -> {
          state.copy(
            words = state.words?.toMutableList()?.apply {
              state.last?.let {
                this[it] = this[it].copy(isSelected = false)
              }
              result.current?.let {
                this[it] = this[it].copy(isSelected = true)
              }
            },
            last = result.current
          )
        }
      }
    }

  override val stateLiveData: LiveData<WordListState> =
    MutableLiveData<WordListState>().apply {
      intents.compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(WordListState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }
}