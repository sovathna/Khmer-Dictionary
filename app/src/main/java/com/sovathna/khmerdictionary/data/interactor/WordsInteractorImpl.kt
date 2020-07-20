package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.data.interactor.base.WordsInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordsInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : WordsInteractor() {

  override val getWords =
    ObservableTransformer<WordsIntent.GetWords, WordsResult> {
      it.flatMap {
        repository
          .getWordsPager()
          .subscribeOn(Schedulers.io())
          .map(WordsResult::PagingSuccess)
      }
    }

  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, WordsResult> {
      it.flatMap { intent ->
        repository
          .selectWord(intent.word?.id)
          .subscribeOn(Schedulers.io())
          .map { WordsResult.SelectWordSuccess }
      }
    }
}