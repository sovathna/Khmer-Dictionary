package com.sovathna.khmerdictionary.model.result

import androidx.paging.Pager
import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.entity.HistoryUI

sealed class HistoriesResult : MviResult {
  data class Success(
    val historiesPager: Pager<Int, HistoryUI>
  ) : HistoriesResult()

  object SelectWordSuccess : HistoriesResult()

  object ClearHistoriesSuccess : HistoriesResult()
}