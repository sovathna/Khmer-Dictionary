package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.Event
import com.sovathna.androidmvi.state.MviState

data class DownloadState(
  val isInit: Boolean = true,
  val error: String? = null,
  val download: Long = 0,
  val total: Long = 0,
  val successEvent: Event<Unit>? = null
) : MviState