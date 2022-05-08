package io.github.sovathna.khmerdictionary.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import io.github.sovathna.khmerdictionary.config.Const
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import io.github.sovathna.khmerdictionary.model.WordEntity
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

  private val dao = appDatabase.wordDao()

  private var job: Job? = null

  fun search(searchTerm: String, type: HomeType) {
    job?.cancel()
    job = viewModelScope.launch {
      Pager(config = PagingConfig(pageSize = Const.PAGE_SIZE)) {
        when (type) {
          HomeType.ALL -> dao.filteredWords("$searchTerm%")
          HomeType.FAVORITE -> dao.filteredBookmarks("$searchTerm%")
          HomeType.HISTORY -> dao.filteredHistories("$searchTerm%")
        }
      }.flow
        .flowOn(ioDispatcher)
        .cachedIn(viewModelScope)
        .distinctUntilChanged()
        .collectLatest {
          Timber.e("search: $searchTerm")
          setState(current.copy(paging = it))
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

  fun updateHistory(word: WordEntity) {
    viewModelScope.launch {
      withContext(ioDispatcher) {
        dao.updateHistory(word.id)
      }
    }
  }
}