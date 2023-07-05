package io.github.sovathna.khmerdictionary.ui.words.home

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.data.db.DictDao
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsViewModel
import io.github.sovathna.khmerdictionary.ui.words.WordsType
import javax.inject.Inject

@HiltViewModel
class WordsViewModel @Inject constructor(
    private val dictDao: DictDao
) : AbstractWordsViewModel() {

    init {
        getWords(WordsType.Words)
    }

    override suspend fun getData(): List<WordUi> = dictDao.get((current.page - 1) * Const.PAGE_SIZE)

}