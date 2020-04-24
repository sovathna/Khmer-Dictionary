package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.result.SplashResult
import io.reactivex.ObservableTransformer

abstract class SplashInteractor : MviInteractor<SplashIntent, SplashResult>() {

  abstract val checkDatabase: ObservableTransformer<SplashIntent.CheckDatabase, SplashResult>

  override val intentsProcessor = ObservableTransformer<SplashIntent, SplashResult> {
    it.publish { intent ->
      intent.ofType(SplashIntent.CheckDatabase::class.java).compose(checkDatabase)
    }
  }
}