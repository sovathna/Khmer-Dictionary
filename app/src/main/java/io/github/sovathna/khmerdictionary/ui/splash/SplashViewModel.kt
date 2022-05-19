package io.github.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.data.interactors.DownloadInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  private val interactor: DownloadInteractor
) : BaseViewModel<SplashState>(SplashState()) {

  init {
    downloadDatabase()
  }

  private var job: Job? = null

  fun downloadDatabase() {
    job?.cancel()
    job = viewModelScope.launch {
      interactor.downloadFlow()
        .distinctUntilChanged()
        .buffer()
        .collectLatest { result ->
          setState(mapDownloadState(result))
        }
    }
  }

  private fun mapDownloadState(result: DownloadInteractor.Result): SplashState {
    return when (result) {
      is DownloadInteractor.Result.Downloading ->
        current.copy(read = result.read, size = result.size, error = null)
      is DownloadInteractor.Result.Done ->
        current.copy(read = 1.0, size = 1.0, redirectEvent = Event(Unit))
      is DownloadInteractor.Result.Error ->
        current.copy(error = result.error)
    }
  }

}