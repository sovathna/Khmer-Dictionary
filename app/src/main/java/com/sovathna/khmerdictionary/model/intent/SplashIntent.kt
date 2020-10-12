package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import java.io.File

sealed class SplashIntent : MviIntent {

  data class CheckDatabase(
    val db: File,
    val tmpDb: File
  ) : SplashIntent()

}