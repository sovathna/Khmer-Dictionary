package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import java.io.File

sealed class SplashIntent : MviIntent {
  data class CheckDatabase(val file: File) : SplashIntent()
}