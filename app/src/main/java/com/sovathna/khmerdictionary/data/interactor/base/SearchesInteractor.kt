package com.sovathna.khmerdictionary.data.interactor.base

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.SearchesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class SearchesInteractor :
  MviInteractor<MviIntent, SearchesResult>() {

  protected abstract val getSearches:
      ObservableTransformer<SearchesIntent.GetWords, SearchesResult>

  protected abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, SearchesResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, SearchesResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(SearchesIntent.GetWords::class.java)
            .compose(getSearches),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord)
        )
      }
    }
}