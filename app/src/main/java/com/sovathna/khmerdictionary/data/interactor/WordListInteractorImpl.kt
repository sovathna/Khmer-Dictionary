package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordListInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : WordListInteractor() {

  override val getWordList = ObservableTransformer<WordListIntent.Get, WordListResult> {
    it.flatMap { intent ->
      repository.getWordList(intent.filter, intent.offset)
        .toObservable()
        .map(WordListResult::Success)
        .subscribeOn(Schedulers.io())
    }
  }
}