package com.sovathna.khmerdictionary.model.state

import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.state.MviState

data class HistoriesState(
  val loadSuccess: Event<Unit>? = null
) : MviState