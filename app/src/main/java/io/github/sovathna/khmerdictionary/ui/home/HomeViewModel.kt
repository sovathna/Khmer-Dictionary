package io.github.sovathna.khmerdictionary.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.config.Const
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  appDatabase: AppDatabase,
  private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<HomeState>(HomeState()) {

  private val dao = appDatabase.wordDao()

  private var job: Job? = null

  fun search(searchTerm: String, type: HomeType) {
    val term = searchTerm.trim()

    if (current.searchTerm == term) return
    job?.cancel()
    job = viewModelScope.launch {
      val filter = "$searchTerm%"
      Pager(config = PagingConfig(pageSize = Const.PAGE_SIZE)) {
        when (type) {
          HomeType.ALL -> dao.filteredWords(filter)
          HomeType.FAVORITE -> dao.filteredBookmarks(filter)
          HomeType.HISTORY -> dao.filteredHistories(filter)
        }
      }.flow
        .flowOn(ioDispatcher)
        .cachedIn(viewModelScope)
        .distinctUntilChanged()
        .collectLatest {
          val tmp: Event<Unit>? =
            if (current.searchTerm != null && current.searchTerm != searchTerm) Event(Unit)
            else current.scrollToTopEvent
          setState(current.copy(searchTerm = term, scrollToTopEvent = tmp, paging = it))
        }
    }
  }

}