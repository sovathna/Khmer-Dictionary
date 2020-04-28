package com.sovathna.khmerdictionary.ui.main

import com.sovathna.khmerdictionary.di.scope.MainScope
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class MainModule {
  @Provides
  @MainScope
  fun selectIntent() = PublishSubject.create<WordListIntent.Select>()

  @Provides
  @MainScope
  fun filterIntent() = PublishSubject.create<WordListIntent.Get>()
}