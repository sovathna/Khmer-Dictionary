package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sovathna.khmerdictionary.model.entity.BookmarkUI
import io.reactivex.Single

@Dao
interface BookmarkUIDao {
  @Query("SELECT * FROM bookmarks_ui ORDER BY name")
  fun get(): PagingSource<Int, BookmarkUI>

  @Insert
  fun add(words: List<BookmarkUI>): Single<List<Long>>

  @Query("DELETE FROM bookmarks_ui")
  fun deleteAll(): Single<Int>

  @Query("SELECT id FROM bookmarks_ui WHERE isSelected = 1")
  fun getSelected(): Single<Long>

  @Query("UPDATE bookmarks_ui SET isSelected = :isSelected WHERE id = :id")
  fun updateSelected(id: Long, isSelected: Boolean): Single<Int>
}