package com.sovathna.khmerdictionary.model.result

import androidx.paging.Pager
import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.entity.WordUI

sealed class WordsResult : MviResult {
  data class PagingSuccess(
    val wordsPager: Pager<Int, WordUI>
  ) : WordsResult()

  object SelectWordSuccess : WordsResult()
}