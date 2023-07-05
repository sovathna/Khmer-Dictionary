package io.github.sovathna.khmerdictionary.ui.words

import androidx.lifecycle.viewModelScope
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import io.github.sovathna.khmerdictionary.ui.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class AbstractWordsViewModel : BaseViewModel<WordsState>(WordsState()) {

  protected abstract suspend fun getData(): List<WordUi>?

  fun getWords(type: WordsType) {
    if (current.isLoading) return
    viewModelScope.launch {
      var page = current.page
      if (type is WordsType.Searches && type != current.type) {
        page = 1
      }
      setState(current.copy(isLoading = true, type = type, page = page))
      val words = getData()
      Timber.tag("debug").d("get words: ${current.page} ${current.type}")
      val newWords = if (current.page > 1) {
        current.words?.toMutableList()?.apply { words?.let { addAll(words) } } ?: words
      } else {
        words
      }
      setState(
        current.copy(
          isLoading = false,
          words = newWords,
          isMore = (words?.size ?: 0) >= Const.PAGE_SIZE
        )
      )
    }
  }

  fun getMore() {
    if (!current.isMore) return
    setState(current.copy(page = current.page + 1))
    getWords(current.type)
  }
}