package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.sovathna.khmerdictionary.model.entity.WordUI
import io.reactivex.Single

@Dao
interface WordUIDao {
  @Query("SELECT * FROM words_ui ORDER BY name")
  fun get(): PagingSource<Int, WordUI>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun add(words: List<WordUI>): Single<List<Long>>

  @Query("DELETE FROM words_ui")
  fun deleteAll(): Single<Int>

  @Query("UPDATE words_ui SET isSelected = 0 WHERE isSelected = 1")
  fun deselectAll(): Int

  @Query("UPDATE words_ui SET isSelected = :isSelected WHERE id = :id")
  fun updateSelected(id: Long, isSelected: Boolean): Int

  @Query("SELECT COUNT(*) FROM words_ui")
  fun count(): Single<Int>

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