package io.github.sovathna.khmerdictionary.ui.words.bookmark

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.data.AppSettings
import io.github.sovathna.khmerdictionary.data.db.LocalDao
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsViewModel
import io.github.sovathna.khmerdictionary.ui.words.WordsType
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
  private val localDao: LocalDao,
  settings: AppSettings
) : AbstractWordsViewModel(settings) {

  init {
    getWords()
  }

  fun getWords() {
    getWords(WordsType.Bookmarks, 1)
  }

  override suspend fun getData(): List<WordUi> =
    localDao.getBookmarks()
}