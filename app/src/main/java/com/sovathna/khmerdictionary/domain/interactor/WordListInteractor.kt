package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import io.reactivex.ObservableTransformer

abstract class WordListInteractor : MviInteractor<WordListIntent, WordListResult>() {

  abstract val getWordList: ObservableTransformer<WordListIntent.Get, WordListResult>

  override val intentsProcessor = ObservableTransformer<WordListIntent, WordListResult> {
    it.publish { intent ->
      intent.ofType(WordListIntent.Get::class.java).compose(getWordList)
    }
  }
}