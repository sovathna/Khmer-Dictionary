package com.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.LiveData
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.result.SplashResult
import com.sovathna.khmerdictionary.domain.model.state.SplashState
import io.reactivex.functions.BiFunction

class SplashViewModel : MviViewModel<SplashIntent,SplashResult,SplashState>(){
  override val reducer: BiFunction<SplashState, SplashResult, SplashState>
    get() = TODO("Not yet implemented")
  override val stateLiveData: LiveData<SplashState>
    get() = TODO("Not yet implemented")
}