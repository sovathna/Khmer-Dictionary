package com.sovathna.khmerdictionary.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
class MainModule {

  @Provides
  @ActivityScoped
  fun clickWordSubject() = PublishSubject.create<Event<Word>>()

  @Provides
  @ActivityScoped
  fun selectWordIntent() = BehaviorSubject.create<WordsIntent.SelectWord>()

  @Provides
  @ActivityScoped
  fun fabVisibilitySubject() = PublishSubject.create<Boolean>()

  @Provides
  @ActivityScoped
  fun searchWordsIntent() = PublishSubject.create<SearchesIntent.GetWords>()

  @Provides
  @ActivityScoped
  fun bookmarkedLiveData() = MutableLiveData<Boolean>()

  @Provides
  @ActivityScoped
  fun menuItemClickLiveData() = MutableLiveData<Event<String>>()

  @Provides
  @ActivityScoped
  fun recycledViewPool() = RecyclerView.RecycledViewPool()

  @Provides
  @ActivityScoped
  fun clearHistoriesIntent() = PublishSubject.create<HistoriesIntent.ClearHistories>()

  @Provides
  @ActivityScoped
  fun clearBookmarksIntent() = PublishSubject.create<BookmarksIntent.ClearBookmarks>()

  @Provides
  @ActivityScoped
  @Named("clear_menu")
  fun clearMenuItemLiveData() = MutableLiveData<Boolean>()

}