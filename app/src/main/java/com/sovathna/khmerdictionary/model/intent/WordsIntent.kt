package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.Word

sealed class WordsIntent : MviIntent {
  data class SelectWord(
    val word: Word?
  ) : WordsIntent()
}