package com.sovathna.khmerdictionary.data.repository.base

import androidx.paging.Pager
import com.sovathna.khmerdictionary.model.Definition
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.BookmarkUI
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import com.sovathna.khmerdictionary.model.entity.SearchUI
import com.sovathna.khmerdictionary.model.entity.WordUI
import com.sovathna.khmerdictionary.ui.words.WordItem
import io.reactivex.Observable

interface AppRepository {

  fun getWords(offset: Int, pageSize: Int): Observable<List<Word>>

  fun addHistory(word: Word): Observable<Long>

  fun getHistoriesPager(): Observable<Pager<Int, HistoryUI>>

  fun getBookmarksPager(): Observable<Pager<Int, BookmarkUI>>

  fun getDefinition(id: Long): Observable<Definition>

  fun checkBookmark(id: Long): Observable<Boolean>

  fun clearHistories(): Observable<Int>

  fun clearBookmarks(): Observable<Int>

  fun getWordsPager(): Observable<Pager<Int, WordUI>>

  fun selectWord(id: Long?): Observable<Int>

  fun selectSearch(id: Long?): Observable<Int>

  fun selectHistory(word: Word?): Observable<Int>

  fun selectBookmark(id: Long?): Observable<Int>

  fun getSearchesPager(searchTerm: String): Observable<Pager<Int, SearchUI>>

  fun addDeleteBookmark(word: Word): Observable<Boolean>
}