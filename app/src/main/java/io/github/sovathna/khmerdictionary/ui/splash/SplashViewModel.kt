package io.github.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.data.ApiService
import io.github.sovathna.khmerdictionary.data.Repository
import io.github.sovathna.khmerdictionary.domain.DownloadInteractor
import io.github.sovathna.khmerdictionary.domain.ExtractZipInteractor
import io.github.sovathna.khmerdictionary.ui.BaseViewModel
import io.github.sovathna.khmerdictionary.ui.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val downloader: DownloadInteractor,
    private val extractor: ExtractZipInteractor,
    private val apiService: ApiService,
    private val repository: Repository
) : BaseViewModel<SplashState>(SplashState()) {

    init {
        init()
    }

    fun init() {
        viewModelScope.launch {
            try {
                setState(current.copy(isDownloading = null, error = null))
                delay(1000)
                val config = apiService.getConfig(Const.CONFIG_URL)
                Const.config = config
                check()
            } catch (e: Exception) {
                Timber.e(e)
                setState(current.copy(error = "error"))
            }
        }
    }

    private fun check() {
        viewModelScope.launch {
            if (repository.shouldDownloadDb()) {
                download()
            } else {
                setState(current.copy(doneEvent = Event(Unit)))
            }
        }
    }

    private fun download() {
        viewModelScope.launch {
            downloader(Const.DB_URL)
                .distinctUntilChanged()
                .collectLatest {
                    when (it) {
                        is DownloadInteractor.Result.Done -> extract(it.file)
                        is DownloadInteractor.Result.Downloading -> {
                            setState(
                                current.copy(
                                    isDownloading = true,
                                    size = it.size,
                                    read = it.read,
                                    error = null,
                                )
                            )
                        }

                        is DownloadInteractor.Result.Error -> {
                            setState(
                                current.copy(
                                    isDownloading = true,
                                    error = "error",
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun extract(file: File) {
        viewModelScope.launch {
            extractor(file)
                .distinctUntilChanged()
                .collectLatest {
                    when (it) {
                        is ExtractZipInteractor.Result.Done -> {
                            repository.setDbVersion(Const.config.version)
                            setState(current.copy(read = current.size, doneEvent = Event(Unit)))
                        }

                        is ExtractZipInteractor.Result.Extracting -> {
                            setState(
                                current.copy(
                                    isDownloading = false,
                                    size = it.size,
                                    read = it.read,
                                    error = null,
                                )
                            )
                        }

                        is ExtractZipInteractor.Result.Error -> {
                            setState(
                                current.copy(
                                    isDownloading = false,
                                    error = "error",
                                )
                            )
                        }
                    }
                }
        }
    }

}