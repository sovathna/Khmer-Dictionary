package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.domain.model.WordList

sealed class WordListResult : MviResult {
    data class Success(val wordList: WordList) : WordListResult()
}