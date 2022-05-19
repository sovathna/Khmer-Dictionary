package io.github.sovathna.khmerdictionary.ui.words.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.config.Const
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  appDatabase: AppDatabase,
  ioDispatcher: CoroutineDispatcher
) : AbstractWordsViewModel(appDatabase, ioDispatcher) {

  override fun search(searchTerm: String) {
    val newTerm = searchTerm.trim()
    if (current.searchTerm == newTerm) return
    searchJob?.cancel()
    searchJob = viewModelScope.launch {
      val filter = "$newTerm%"
      Pager(config = PagingConfig(pageSize = Const.PAGE_SIZE)) {
        dao.filteredWords(filter)
      }.flow
        .flowOn(ioDispatcher)
        .cachedIn(viewModelScope)
        .distinctUntilChanged()
        .collectLatest {
          setWordsState(newTerm, it)
        }
    }
  }
}