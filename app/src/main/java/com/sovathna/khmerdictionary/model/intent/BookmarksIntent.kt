package com.sovathna.khmerdictionary.model.intent

import com.sovathna.androidmvi.intent.MviIntent

sealed class BookmarksIntent : MviIntent {
  object GetWords : BookmarksIntent()

  object ClearBookmarks : BookmarksIntent()
}