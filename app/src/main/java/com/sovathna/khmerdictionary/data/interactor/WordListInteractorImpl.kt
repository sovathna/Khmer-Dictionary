package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordListInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : WordListInteractor() {

  override val filterWordList = ObservableTransformer<WordListIntent.Filter, WordListResult> {
    it.flatMap { intent ->
      repository.filterWordList(intent.filter, intent.offset, Const.PAGE_SIZE)
        .toObservable()
        .map { words ->
          WordListResult.Success(
            words,
            intent.offset != 0,
            intent.offset == 0
          )
        }
        .subscribeOn(Schedulers.io())
    }
  }

  override val select = ObservableTransformer<WordListIntent.Select, WordListResult> {
    it.map { intent ->
      WordListResult.Select(intent.current)
    }
  }
}