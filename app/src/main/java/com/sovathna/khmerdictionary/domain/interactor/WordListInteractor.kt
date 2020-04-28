package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class WordListInteractor : MviInteractor<WordListIntent, WordListResult>() {

  abstract val getWordList: ObservableTransformer<WordListIntent.Get, WordListResult>

  abstract val select: ObservableTransformer<WordListIntent.Select, WordListResult>

  override val intentsProcessor = ObservableTransformer<WordListIntent, WordListResult> {
    it.publish { intent ->
      Observable.merge(
        intent.ofType(WordListIntent.Get::class.java).compose(getWordList),
        intent.ofType(WordListIntent.Select::class.java).compose(select)
      )
    }
  }
}