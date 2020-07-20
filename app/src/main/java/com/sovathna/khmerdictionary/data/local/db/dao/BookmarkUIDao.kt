package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sovathna.khmerdictionary.model.entity.BookmarkUI
import io.reactivex.Single

@Dao
interface BookmarkUIDao {
  @Query("SELECT * FROM bookmarks_ui ORDER BY uid DESC")
  fun get(): PagingSource<Int, BookmarkUI>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(words: List<BookmarkUI>): Single<List<Long>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(word: BookmarkUI): Single<Long>

  @Query("DELETE FROM bookmarks_ui WHERE id = :id")
  fun delete(id: Long): Single<Int>

  @Query("DELETE FROM bookmarks_ui")
  fun clear(): Single<Int>

  @Query("UPDATE bookmarks_ui SET isSelected = 0 WHERE isSelected = 1")
  fun deselectAll(): Single<Int>

  @Query("UPDATE bookmarks_ui SET isSelected = :isSelected WHERE id = :id")
  fun updateSelected(id: Long, isSelected: Boolean): Single<Int>
}