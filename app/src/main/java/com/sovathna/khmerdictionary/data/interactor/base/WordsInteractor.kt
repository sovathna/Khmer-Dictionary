package com.sovathna.khmerdictionary.data.interactor.base

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.WordsResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class WordsInteractor :
  MviInteractor<MviIntent, WordsResult>() {

  protected abstract val getWords:
      ObservableTransformer<WordsIntent.GetWords, WordsResult>

  protected abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, WordsResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, WordsResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(WordsIntent.GetWords::class.java)
            .compose(getWords),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord)
        )
      }
    }
}