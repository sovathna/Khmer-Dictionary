package com.sovathna.khmerdictionary.ui.splash

import com.sovathna.androidmvi.activity.MviActivity
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.state.SplashState
import io.reactivex.Observable

class SplashActivity : MviActivity<SplashIntent, SplashState, SplashViewModel>(
  R.layout.activity_splash
) {

  override fun intents(): Observable<SplashIntent> {
    TODO("Not yet implemented")
  }

  override fun render(state: SplashState) {
    TODO("Not yet implemented")
  }
}