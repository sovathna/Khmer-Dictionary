package io.github.sovathna.khmerdictionary.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.config.Const
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import io.github.sovathna.khmerdictionary.model.WordEntity
import io.github.sovathna.khmerdictionary.ui.detail.DetailState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class HomeViewModel @Inject constructor(
  appDatabase: AppDatabase,
  private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<HomeState>(HomeState()) {

  init {
    Timber.d("init")
  }

  private val dao = appDatabase.wordDao()

  private var job: Job? = null

  private var mSearchTerm: String? = null

  fun search(searchTerm: String, type: HomeType) {
    if (mSearchTerm == searchTerm) return
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
            if (mSearchTerm != null && mSearchTerm != searchTerm) Event(Unit)
            else current.scrollToTopEvent
          setState(current.copy(scrollToTopEvent = tmp, paging = it))
          mSearchTerm = searchTerm
        }
    }
  }

  fun updateBookmark(word: WordEntity) {
    viewModelScope.launch {
      withContext(ioDispatcher) {
        dao.updateBookmark(word.id, !word.favorite)
      }
    }
  }

  fun select(word: WordEntity, detail: DetailState?) {
    viewModelScope.launch {
      withContext(ioDispatcher) {
        detail?.id?.let {
          dao.updateSelection(it, false)
        }
        dao.updateHistory(word.id)
        dao.updateSelection(word.id, true)
      }
      setState(current.copy(selectEvent = Event(Unit)))
    }
  }
}