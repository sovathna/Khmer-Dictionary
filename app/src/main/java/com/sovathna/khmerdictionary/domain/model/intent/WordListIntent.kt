package com.sovathna.khmerdictionary.domain.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class WordListIntent : MviIntent {
    object Get : WordListIntent()
}