package com.sovathna.khmerdictionary.data

import com.sovathna.khmerdictionary.data.interactor.DefinitionInteractorImpl
import com.sovathna.khmerdictionary.data.interactor.SplashInteractorImpl
import com.sovathna.khmerdictionary.data.interactor.WordListInteractorImpl
import com.sovathna.khmerdictionary.domain.interactor.DefinitionInteractor
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import dagger.Binds
import dagger.Module

@Module
abstract class InteractorModule {

  @Binds
  abstract fun splashInteractor(impl: SplashInteractorImpl): SplashInteractor

  @Binds
  abstract fun wordListInteractor(impl: WordListInteractorImpl): WordListInteractor

  @Binds
  abstract fun definitionInteractor(impl: DefinitionInteractorImpl): DefinitionInteractor

}