package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class WordListIntent : MviIntent {
  data class Get(val filter: String?, val offset: Int) : WordListIntent()
}