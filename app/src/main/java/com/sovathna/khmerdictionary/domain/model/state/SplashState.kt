package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.Event
import com.sovathna.androidmvi.state.MviState

data class SplashState(
  val isInit: Boolean = true,
  val error: String? = null,
  val exists: Event<Boolean>? = null
) : MviState