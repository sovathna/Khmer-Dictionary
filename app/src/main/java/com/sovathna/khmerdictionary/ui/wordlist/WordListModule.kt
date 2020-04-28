package com.sovathna.khmerdictionary.ui.wordlist

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class WordListModule {

  @Provides
  fun viewModel(fragment: WordListFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[WordListViewModel::class.java]

  @Provides
  fun getWordListIntent() = PublishSubject.create<WordListIntent.Get>()

  @Provides
  fun layoutManager(activity: MainActivity): RecyclerView.LayoutManager =
    LinearLayoutManager(activity)

}