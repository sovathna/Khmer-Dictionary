package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.sovathna.khmerdictionary.model.entity.SearchUI
import io.reactivex.Single

@Dao
interface SearchUIDao {
  @Query("SELECT * FROM searches_ui ORDER BY name")
  fun get(): PagingSource<Int, SearchUI>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun add(words: List<SearchUI>): Single<List<Long>>

  @Query("DELETE FROM searches_ui WHERE 1")
  fun deleteAll(): Single<Int>

  @Query("UPDATE searches_ui SET isSelected = 0 WHERE isSelected = 1")
  fun deselectAll(): Int

  @Query("UPDATE searches_ui SET isSelected = :isSelected WHERE id = :id")
  fun updateSelected(id: Long, isSelected: Boolean): Int

  @Transaction
  fun selectWord(id: Long?): Int {
    return if (id != null) {
      deselectAll()
      updateSelected(id, true)
    } else {
      deselectAll()
    }
  }
}