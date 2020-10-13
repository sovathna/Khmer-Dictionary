package com.sovathna.khmerdictionary.data.interactor.impl

import com.sovathna.khmerdictionary.data.interactor.base.BookmarksInteractor
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.result.BookmarksResult
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookmarksInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : BookmarksInteractor() {
  override val getBookmarks =
    ObservableTransformer<BookmarksIntent.GetWords, BookmarksResult> {
      it.flatMap { intent ->
        repository
          .getBookmarksPager()
          .subscribeOn(Schedulers.io())
          .map { BookmarksResult.Success(it) }
          .subscribeOn(Schedulers.computation())
      }
    }

  override val selectWord =
    ObservableTransformer<WordsIntent.SelectWord, BookmarksResult> {
      it.flatMap { intent ->
        repository
          .selectBookmark(intent.word?.id)
          .subscribeOn(Schedulers.io())
          .map { BookmarksResult.SelectWordSuccess }
      }
    }

  override val clearBookmark =
    ObservableTransformer<BookmarksIntent.ClearBookmarks, BookmarksResult> {
      it.flatMap {
        repository
          .clearBookmarks()
          .subscribeOn(Schedulers.io())
          .map { BookmarksResult.ClearBookmarkSuccess }
      }
    }
}