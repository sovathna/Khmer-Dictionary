package com.sovathna.khmerdictionary.data.interactor.impl

import com.sovathna.khmerdictionary.data.interactor.base.HistoriesInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.HistoriesResult
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoriesInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : HistoriesInteractor() {
  override val getHistories =
    ObservableTransformer<HistoriesIntent.GetWords, HistoriesResult> {
      it.flatMap { intent ->
        repository
          .getHistoriesPager()
          .subscribeOn(Schedulers.io())
          .map { HistoriesResult.Success(it) }
      }
    }

  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, HistoriesResult> {
      it.flatMap { intent ->
        repository
          .selectHistory(intent.word)
          .subscribeOn(Schedulers.io())
          .map { HistoriesResult.SelectWordSuccess }
      }
    }

  override val clearHistories =
    ObservableTransformer<HistoriesIntent.ClearHistories, HistoriesResult> {
      it.flatMap {
        repository
          .clearHistories()
          .subscribeOn(Schedulers.io())
          .map { HistoriesResult.ClearHistoriesSuccess }
      }
    }
}