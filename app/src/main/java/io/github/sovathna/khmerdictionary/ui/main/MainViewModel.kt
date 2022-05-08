package io.github.sovathna.khmerdictionary.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainState>(MainState()) {

  private val _searchFlow = MutableStateFlow("")
  val searchFlow: Flow<String> = _searchFlow

  fun search(searchTerm: String) {
    _searchFlow.value = searchTerm
  }

  fun updateSearchState() {
    val isSearch = current.isSearch
    setState(current.copy(isSearch = !isSearch))
  }
}