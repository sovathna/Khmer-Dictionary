package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class DefinitionIntent : MviIntent {
  data class Get(val id: Long) : DefinitionIntent()
}