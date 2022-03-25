package io.github.sovathna.khmerdictionary.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.config.Const
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
class HomeViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {

    private val state = MutableLiveData(HomeState())
    val stateLiveData: LiveData<HomeState> = state
    private val current get() = state.value!!

    val words = Pager(config = PagingConfig(pageSize = Const.PAGE_SIZE)) {
        appDatabase.wordDao().homeWords()
    }.flow.cachedIn(viewModelScope)

    val filtered = Pager(config = PagingConfig(pageSize = Const.PAGE_SIZE)) {
        appDatabase.wordDao().filteredWords("ខ%")
    }.flow

    private fun setState(state: HomeState) {
        this.state.value = state
    }
}