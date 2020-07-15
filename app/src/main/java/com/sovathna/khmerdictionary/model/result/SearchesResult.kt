package com.sovathna.khmerdictionary.model.result

import androidx.paging.Pager
import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.entity.SearchUI

sealed class SearchesResult : MviResult {
  data class Success(
    val searchesPager: Pager<Int, SearchUI>
  ) : SearchesResult()

  object SelectWordSuccess : SearchesResult()
}