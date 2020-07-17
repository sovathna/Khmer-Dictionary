package com.sovathna.khmerdictionary.data.interactor.base

import androidx.paging.Pager
import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.entity.SearchUI
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.SearchesResult
import io.reactivex.ObservableTransformer

abstract class SearchesInteractor :
  MviInteractor<MviIntent, SearchesResult>() {

  abstract fun getSearches(searchTerm: String): Pager<Int, SearchUI>

  protected abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, SearchesResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, SearchesResult> {
      it.publish { intent ->
        intent
          .ofType(WordsIntent.SelectWord::class.java)
          .compose(selectWord)
      }
    }
}