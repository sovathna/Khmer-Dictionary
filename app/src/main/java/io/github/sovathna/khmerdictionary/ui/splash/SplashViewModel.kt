package io.github.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.data.interactors.DownloadInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val interactor: DownloadInteractor
) : ViewModel() {

    private val state = MutableLiveData(SplashState())
    val stateLiveData: LiveData<SplashState> = state
    private val current get() = state.value!!

    init {
        downloadDatabase()
    }

    private fun downloadDatabase() {
        viewModelScope.launch {
            interactor.downloadFlow()
                .distinctUntilChanged()
                .collectLatest { result ->
                    Timber.d("download result: $result")
                    when (result) {
                        is DownloadInteractor.Result.Downloading ->
                            setState(current.copy(read = result.read, size = result.size))
                        is DownloadInteractor.Result.Done -> {
                            setState(current.copy(read = 1.0, size = 1.0))
                            redirect()
                        }
                        is DownloadInteractor.Result.Error ->
                            setState(current.copy(error = result.error))
                    }
                }
        }
    }

    private fun setState(state: SplashState) {
        this.state.value = state
    }

    private fun redirect() {
        viewModelScope.launch {
            delay(2000)
            setState(current.copy(redirectEvent = Event(Unit)))
        }
    }

}