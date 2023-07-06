package io.github.sovathna.khmerdictionary.ui.words

import androidx.lifecycle.viewModelScope
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.data.AppSettings
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import io.github.sovathna.khmerdictionary.ui.BaseViewModel
import io.github.sovathna.khmerdictionary.ui.Event
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class AbstractWordsViewModel(
  private val settings: AppSettings
) : BaseViewModel<WordsState>(WordsState()) {

  protected abstract suspend fun getData(): List<WordUi>?

  fun getWords(type: WordsType, page: Int) {
    if (current.isLoading) return
    viewModelScope.launch {
      setState(current.copy(isLoading = true, type = type, page = page))
      val words = getData()
      Timber.tag("debug").d("get words: ${current.page} ${current.type}")
      val newWords = if (current.page > 1) {
        current.words?.toMutableList()?.apply { words?.let { addAll(words) } } ?: words
      } else {
        words
      }
      val isMore = if (type is WordsType.Words) {
        (words?.size ?: 0) >= Const.PAGE_SIZE
      } else {
        false
      }
      setState(
        current.copy(
          isLoading = false,
          words = newWords,
          isMore = isMore
        )
      )
    }
  }

  fun select(id: Long) {
    viewModelScope.launch {
      settings.setDetailId(id)
      setState(current.copy(detailEvent = Event(Unit)))
    }
  }

  fun getMore() {
    if (!current.isMore) return
    getWords(current.type, current.page + 1)
  }
}