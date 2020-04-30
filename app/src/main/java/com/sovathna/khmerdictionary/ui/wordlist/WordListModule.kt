package com.sovathna.khmerdictionary.ui.wordlist

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class WordListModule {

  @Provides
  fun viewModel(fragment: WordListFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[WordListViewModel::class.java]

  @Provides
  @Named("filter")
  fun filterIntent() = PublishSubject.create<WordListIntent.Filter>()

  @Provides
  @Named("search")
  fun searchIntent() = PublishSubject.create<WordListIntent.Filter>()

  @Provides
  fun selectIntent() = PublishSubject.create<WordListIntent.Select>()

  @Provides
  fun layoutManager(activity: MainActivity): RecyclerView.LayoutManager =
    LinearLayoutManager(activity)

}