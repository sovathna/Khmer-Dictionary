package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.data.interactor.base.SearchesInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.SearchesResult
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchesInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : SearchesInteractor() {
  override val getWords =
    ObservableTransformer<SearchesIntent.GetWords, SearchesResult> {
      it.flatMap { intent ->
        repository
          .getSearchesPager(intent.searchTerm)
          .subscribeOn(Schedulers.io())
          .map(SearchesResult::Success)
      }
    }
  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, SearchesResult> {
      it.flatMap { intent ->
        repository
          .selectSearch(intent.word?.id)
          .subscribeOn(Schedulers.io())
          .map { SearchesResult.SelectWordSuccess }
      }
    }
}