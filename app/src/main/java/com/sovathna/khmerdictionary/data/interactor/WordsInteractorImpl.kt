package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordsInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : MviInteractor<WordsIntent, WordsResult>() {

  fun getWords() = repository.getWordsPager()

  private val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, WordsResult> {
      it.flatMap { intent ->
        repository
          .selectWord(intent.word?.id)
          .subscribeOn(Schedulers.io())
          .map { WordsResult.SelectWordSuccess }
      }
    }

  override val intentsProcessor =
    ObservableTransformer<WordsIntent, WordsResult> {
      it.publish { intent ->
        intent
          .ofType(WordsIntent.SelectWord::class.java)
          .compose(selectWord)
      }
    }
}