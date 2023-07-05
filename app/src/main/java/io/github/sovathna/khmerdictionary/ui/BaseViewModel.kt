package io.github.sovathna.khmerdictionary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.internal.ThreadUtil

abstract class BaseViewModel<S> constructor(initState: S) : ViewModel() {

  private val _state = MutableLiveData(initState)
  val stateLiveData: LiveData<S> = _state
  val current get() = _state.value!!

  protected fun setState(s: S) {
    ThreadUtil.ensureMainThread()
    _state.value = s
  }

}