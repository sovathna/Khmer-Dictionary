package com.sovathna.khmerdictionary.model.result

import androidx.paging.Pager
import com.sovathna.androidmvi.result.MviResult
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.BookmarkUI

sealed class BookmarksResult : MviResult {
  data class Success(
    val bookmarksPager: Pager<Int, BookmarkUI>
  ) : BookmarksResult()

  object SelectWordSuccess : BookmarksResult()

  object ClearBookmarkSuccess : BookmarksResult()
}