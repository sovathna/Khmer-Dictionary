package io.github.sovathna.khmerdictionary.ui.words.search

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.data.db.DictDao
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsViewModel
import io.github.sovathna.khmerdictionary.ui.words.WordsType
import javax.inject.Inject

@HiltViewModel
class SearchesViewModel @Inject constructor(
  private val dictDao: DictDao
) : AbstractWordsViewModel() {

  override suspend fun getData(): List<WordUi>? {
    val type = current.type
    if (type !is WordsType.Searches) return null
    if (type.searchTerm.isBlank()) return null
    return dictDao.filter("${type.searchTerm}%", (current.page - 1) * Const.PAGE_SIZE)
  }

}