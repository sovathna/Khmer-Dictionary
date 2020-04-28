package com.sovathna.khmerdictionary.vm

import androidx.lifecycle.ViewModel
import com.sovathna.khmerdictionary.di.ViewModelKey
import com.sovathna.khmerdictionary.domain.interactor.DefinitionInteractor
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.ui.definition.DefinitionViewModel
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
  @ViewModelKey(WordListViewModel::class)
  fun wordListViewModel(interactor: WordListInteractor): ViewModel =
    WordListViewModel(interactor)

  @Provides
  @IntoMap
  @ViewModelKey(DefinitionViewModel::class)
  fun definitionViewModel(interactor: DefinitionInteractor): ViewModel =
    DefinitionViewModel(interactor)

}

