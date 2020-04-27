package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.Event
import com.sovathna.androidmvi.state.MviState

data class SplashState(
  val isInit: Boolean = true,
  val isProgress: Boolean = false,
  val error: String? = null,
  val downloaded: Long? = null,
  val total: Long? = null,
  val successEvent: Event<Unit>? = null
) : MviState