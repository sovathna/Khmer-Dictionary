package com.sovathna.khmerdictionary.domain.model.result

import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.domain.model.Word

sealed class WordListResult : MviResult {
  data class Success(
    val words: List<Word>,
    val isMore: Boolean,
    val isReset: Boolean
  ) : WordListResult()

  data class Select(val current: Long?) : WordListResult()
}