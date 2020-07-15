package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sovathna.khmerdictionary.model.entity.SearchUI
import io.reactivex.Single

@Dao
interface SearchUIDao {
  @Query("SELECT * FROM searches_ui ORDER BY name")
  fun get(): PagingSource<Int, SearchUI>

  @Insert
  fun add(words: List<SearchUI>): Single<List<Long>>

  @Query("DELETE FROM searches_ui")
  fun deleteAll(): Single<Int>

  @Query("UPDATE searches_ui SET isSelected = 0 WHERE isSelected = 1")
  fun deselectAll(): Single<Int>

  @Query("UPDATE searches_ui SET isSelected = :isSelected WHERE id = :id")
  fun updateSelected(id: Long, isSelected: Boolean): Single<Int>
}