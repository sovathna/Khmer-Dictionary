package io.github.sovathna.khmerdictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<S>(initState: S) : ViewModel() {
  private val state = MutableLiveData(initState)
  val stateLiveData: LiveData<S> = state
  val current get() = state.value!!

  protected fun setState(state: S) {
    this.state.value = state
  }
}