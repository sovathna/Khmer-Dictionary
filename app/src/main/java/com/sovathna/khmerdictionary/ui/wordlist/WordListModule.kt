package com.sovathna.khmerdictionary.ui.wordlist

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class WordListModule {

  @Provides
  fun viewModel(fragment: WordListFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[WordListViewModel::class.java]

  @Provides
  fun getWordListIntent() = PublishSubject.create<WordListIntent.Get>().also {
    Log.d("===", "Init intent")
  }

}