package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class SplashIntent : MviIntent {
  object CheckDatabase : SplashIntent()
}