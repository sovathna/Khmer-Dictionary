package com.sovathna.khmerdictionary.vm

import androidx.lifecycle.ViewModel
import com.sovathna.khmerdictionary.di.ViewModelKey
import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.ui.wordlist.WordListViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class AppViewModelsModule {

    @Provides
    @IntoMap
    @ViewModelKey(WordListViewModel::class)
    fun wordListViewModel(interactor: WordListInteractor): ViewModel =
        WordListViewModel(interactor)

}

