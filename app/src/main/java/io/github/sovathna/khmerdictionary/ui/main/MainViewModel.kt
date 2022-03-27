package io.github.sovathna.khmerdictionary.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

  private val state = MutableLiveData(MainState())
  val stateLiveData: LiveData<MainState> = state
  private val current get() = state.value!!

  private val _searchFlow = MutableStateFlow("")
  val searchFlow: Flow<String> = _searchFlow

  fun search(searchTerm: String) {
    _searchFlow.value = searchTerm
  }

  fun updateSearchState() {
    val isSearch = current.isSearch
    setState(current.copy(isSearch = !isSearch))
  }

  private fun setState(state: MainState) {
    this.state.value = state
  }

}