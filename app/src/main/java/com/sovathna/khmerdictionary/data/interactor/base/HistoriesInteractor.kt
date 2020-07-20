package com.sovathna.khmerdictionary.data.interactor.base

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.HistoriesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class HistoriesInteractor :
  MviInteractor<MviIntent, HistoriesResult>() {

  protected abstract val getHistories:
      ObservableTransformer<HistoriesIntent.GetWords, HistoriesResult>

  protected abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, HistoriesResult>

  protected abstract val clearHistories:
      ObservableTransformer<HistoriesIntent.ClearHistories, HistoriesResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, HistoriesResult> {
      it.publish { intent ->
        Observable.merge(
          intent
            .ofType(HistoriesIntent.GetWords::class.java)
            .compose(getHistories),
          intent
            .ofType(WordsIntent.SelectWord::class.java)
            .compose(selectWord),
          intent
            .ofType(HistoriesIntent.ClearHistories::class.java)
            .compose(clearHistories)
        )
      }
    }
}