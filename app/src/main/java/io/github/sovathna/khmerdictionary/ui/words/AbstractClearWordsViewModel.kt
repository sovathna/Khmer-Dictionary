package io.github.sovathna.khmerdictionary.ui.words

import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher

abstract class AbstractClearWordsViewModel(appDatabase: AppDatabase, ioDispatcher: CoroutineDispatcher) :
  AbstractWordsViewModel(appDatabase, ioDispatcher) {
  abstract fun clearWords()
}