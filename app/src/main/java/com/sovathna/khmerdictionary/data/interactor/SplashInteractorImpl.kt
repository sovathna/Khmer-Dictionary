package com.sovathna.khmerdictionary.data.interactor

import android.content.Context
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.result.SplashResult
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class SplashInteractorImpl @Inject constructor(
  private val context: Context
) : SplashInteractor() {

  override val checkDatabase = ObservableTransformer<SplashIntent.CheckDatabase, SplashResult> {
    it.map {
      val dbFile = context.getDatabasePath(Const.DB_NAME)
      SplashResult.CheckDatabase(dbFile.exists())
    }
  }
}