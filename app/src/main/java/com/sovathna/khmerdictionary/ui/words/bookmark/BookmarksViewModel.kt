package com.sovathna.khmerdictionary.ui.words.bookmark

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
import com.sovathna.khmerdictionary.data.interactor.base.BookmarksInteractor
import com.sovathna.khmerdictionary.model.result.BookmarksResult
import com.sovathna.khmerdictionary.model.state.BookmarksState
import io.reactivex.BackpressureStrategy
import io.reactivex.functions.BiFunction

class BookmarksViewModel @ViewModelInject constructor(
  private val interactor: BookmarksInteractor
) : MviViewModel<MviIntent, BookmarksResult, BookmarksState>() {

  override val reducer =
    BiFunction<BookmarksState, BookmarksResult, BookmarksState> { state, result ->
      when (result) {
        is BookmarksResult.Success ->
          state.copy(
            isInit = false,
            wordsLiveData = result.bookmarksPager.liveData
              .map { it.map { it.toWordItem() } }
              .cachedIn(viewModelScope),
            loadSuccess = Event(Unit)
          )
        is BookmarksResult.SelectWordSuccess -> state
        is BookmarksResult.ClearBookmarkSuccess -> state
      }
    }

  override val stateLiveData: LiveData<BookmarksState> =
    MutableLiveData<BookmarksState>().apply {
      intents
        .compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .toFlowable(BackpressureStrategy.BUFFER)
        .scan(BookmarksState(), reducer)
        .distinctUntilChanged()
        .subscribe(::postValue)
    }

}