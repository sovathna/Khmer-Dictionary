package com.sovathna.khmerdictionary.ui.definition

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class DefinitionModule {

  @Provides
  fun viewModel(fragment: DefinitionFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[DefinitionViewModel::class.java]

  @Provides
  fun getDefinitionIntent() = PublishSubject.create<DefinitionIntent.Get>()

}