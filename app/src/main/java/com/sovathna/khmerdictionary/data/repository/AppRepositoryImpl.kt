package com.sovathna.khmerdictionary.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.data.local.db.AppDatabase
import com.sovathna.khmerdictionary.data.local.db.LocalDatabase
import com.sovathna.khmerdictionary.data.mediator.BookmarksRemoteMediator
import com.sovathna.khmerdictionary.data.mediator.HistoriesRemoteMediator
import com.sovathna.khmerdictionary.data.mediator.SearchesRemoteMediator
import com.sovathna.khmerdictionary.data.mediator.WordsRemoteMediator
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.Definition
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.BookmarkUI
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import com.sovathna.khmerdictionary.model.entity.SearchUI
import com.sovathna.khmerdictionary.model.entity.WordUI
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
  private val db: AppDatabase,
  private val local: LocalDatabase
) : AppRepository {

  private val wordDao = db.wordDao()
  private val historyDao = local.historyDao()
  private val bookmarkDao = local.bookmarkDao()
  private val wordUIDao = local.wordUIDao()
  private val historyUIDao = local.historyUIDao()
  private val searchUIDao = local.searchUIDao()
  private val bookmarkUIDao = local.bookmarkUIDao()

  override fun getWords(offset: Int, pageSize: Int): Observable<List<Word>> {
    return wordDao
      .get(offset, pageSize)
      .map { entities -> entities.map { entity -> entity.toWord() } }
      .toObservable()
  }

  override fun addHistory(word: Word): Observable<Long> {
    return historyDao.add(word.toHistoryEntity()).toObservable()
  }

  override fun getHistoriesPager(): Observable<Pager<Int, HistoryUI>> {
    return Observable.just(Pager(
      config = PagingConfig(pageSize = Const.PAGE_SIZE),
      remoteMediator = HistoriesRemoteMediator(local),
      pagingSourceFactory = { local.historyUIDao().get() }
    ))
  }

  override fun getDefinition(id: Long): Observable<Definition> =
    wordDao
      .get(id)
      .map {
        Definition(
          it.word,
          it.definition
            .replace("<\"", "<a href=\"")
            .replace("/a", "</a>")
            .replace("\\n", "<br><br>")
            .replace(" : ", " : ឧ. ")
            .replace("ន.", "<span style=\"color:#D50000\">ន.</span>")
            .replace("កិ. វិ.", "<span style=\"color:#D50000\">កិ. វិ.</span>")
            .replace("កិ.វិ.", "<span style=\"color:#D50000\">កិ.វិ.</span>")
            .replace("កិ.", "<span style=\"color:#D50000\">កិ.</span>")
            .replace("និ.", "<span style=\"color:#D50000\">និ.</span>")
            .replace("គុ.", "<span style=\"color:#D50000\">គុ.</span>")
        )
      }
      .toObservable()

  override fun checkBookmark(id: Long): Observable<Boolean> =
    bookmarkDao
      .get(id).map { true }
      .onErrorReturn { false }
      .toObservable()

  override fun clearHistories(): Observable<Int> =
    historyDao
      .clear()
      .flatMap { historyUIDao.clear() }
      .toObservable()

  override fun clearBookmarks(): Observable<Int> =
    bookmarkDao
      .clear()
      .flatMap { bookmarkUIDao.clear() }
      .toObservable()

  override fun getWordsPager(): Observable<Pager<Int, WordUI>> {
    return Observable.fromCallable {
      Pager(
        config = PagingConfig(pageSize = Const.PAGE_SIZE),
        remoteMediator = WordsRemoteMediator(db, local),
        pagingSourceFactory = { local.wordUIDao().get() }
      )
    }
  }

  override fun getSearchesPager(searchTerm: String): Observable<Pager<Int, SearchUI>> {
    return Observable.just(Pager(
      config = PagingConfig(pageSize = Const.PAGE_SIZE),
      remoteMediator = SearchesRemoteMediator("$searchTerm%", db, local),
      pagingSourceFactory = { local.searchUIDao().get() }
    ))
  }

  override fun getBookmarksPager(): Observable<Pager<Int, BookmarkUI>> {
    return Observable.just(Pager(
      config = PagingConfig(pageSize = Const.PAGE_SIZE),
      remoteMediator = BookmarksRemoteMediator(local),
      pagingSourceFactory = { local.bookmarkUIDao().get() }
    ))
  }

  override fun selectWord(id: Long?): Observable<Int> {
    return Observable.fromCallable { wordUIDao.selectWord(id) }
  }

  override fun selectSearch(id: Long?): Observable<Int> {
    return Observable.fromCallable { searchUIDao.selectWord(id) }
  }

  override fun selectHistory(word: Word?): Observable<Int> {
    return Observable.fromCallable { historyUIDao.selectWord(word) }
  }

  override fun selectBookmark(id: Long?): Observable<Int> {
    return Observable.fromCallable { bookmarkUIDao.selectWord(id) }
  }

  override fun addDeleteBookmark(word: Word): Observable<Boolean> {
    return Single.fromCallable {
      bookmarkDao.addDelete(word)
    }.flatMap {
      if (!it) {
        bookmarkUIDao.delete(word.id).map { false }
      } else {
        bookmarkUIDao.add(word.toBookmarkUI()).map { true }
      }
    }.toObservable()
  }
}