package io.github.sovathna.khmerdictionary.ui.words.history

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.data.db.LocalDao
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsViewModel
import io.github.sovathna.khmerdictionary.ui.words.WordsType
import javax.inject.Inject

@HiltViewModel
class HistoriesViewModel @Inject constructor(
  private val localDao: LocalDao
) : AbstractWordsViewModel() {
  init {
    getWords(WordsType.Histories)
  }

  override suspend fun getData(): List<WordUi> =
    localDao.getHistories((current.page - 1) * Const.PAGE_SIZE)
}