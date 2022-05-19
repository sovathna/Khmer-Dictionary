package io.github.sovathna.khmerdictionary.ui.words

import androidx.paging.PagingData
import io.github.sovathna.khmerdictionary.BaseViewModel
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import io.github.sovathna.khmerdictionary.model.WordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job

abstract class AbstractWordsViewModel(
  appDatabase: AppDatabase,
  protected val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<WordsState>(WordsState()) {

  protected val dao = appDatabase.wordDao()

  protected var searchJob: Job? = null
  abstract fun search(searchTerm: String)

  fun setEmptyState(isEmpty: Boolean) {
    setState(current.copy(isEmpty = isEmpty))
  }

  protected fun setWordsState(searchTerm: String, paging: PagingData<WordEntity>) {
    val tmp: Event<Unit>? =
      if (current.searchTerm != null && current.searchTerm != searchTerm) Event(Unit)
      else current.scrollToTopEvent
    setState(current.copy(searchTerm = searchTerm, scrollToTopEvent = tmp, paging = paging))
  }
}