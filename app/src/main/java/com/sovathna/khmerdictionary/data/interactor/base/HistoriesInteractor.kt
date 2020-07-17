package com.sovathna.khmerdictionary.data.interactor.base

import androidx.paging.Pager
import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import com.sovathna.khmerdictionary.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.HistoriesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

abstract class HistoriesInteractor :
  MviInteractor<MviIntent, HistoriesResult>() {

  protected abstract fun getWords(): Pager<Int, HistoryUI>

  protected abstract val selectWord:
      ObservableTransformer<WordsIntent.SelectWord, HistoriesResult>

  protected abstract val clearHistories:
      ObservableTransformer<HistoriesIntent.ClearHistories, HistoriesResult>

  override val intentsProcessor =
    ObservableTransformer<MviIntent, HistoriesResult> {
      it.publish { intent ->
        Observable.merge(
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