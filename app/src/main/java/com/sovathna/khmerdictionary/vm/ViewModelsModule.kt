package com.sovathna.khmerdictionary.vm

import androidx.lifecycle.ViewModel
import com.sovathna.khmerdictionary.di.ViewModelKey
import com.sovathna.khmerdictionary.domain.interactor.DownloadInteractor
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.ui.download.DownloadViewModel
import com.sovathna.khmerdictionary.ui.splash.SplashViewModel
import com.sovathna.khmerdictionary.ui.wordlist.WordListViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelsModule {

  @Provides
  @IntoMap
  @ViewModelKey(SplashViewModel::class)
  fun splashViewModel(interactor: SplashInteractor): ViewModel =
    SplashViewModel(interactor)

  @Provides
  @IntoMap
  @ViewModelKey(DownloadViewModel::class)
  fun downloadViewModel(interactor: DownloadInteractor): ViewModel =
    DownloadViewModel(interactor)

  @Provides
  @IntoMap
  @ViewModelKey(WordListViewModel::class)
  fun wordListViewModel(interactor: WordListInteractor): ViewModel =
    WordListViewModel(interactor)

}

