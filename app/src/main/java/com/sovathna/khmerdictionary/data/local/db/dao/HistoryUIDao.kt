package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import io.reactivex.Single

@Dao
interface HistoryUIDao {
  @Query("SELECT * FROM histories_ui ORDER BY uid DESC")
  fun get(): PagingSource<Int, HistoryUI>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(words: List<HistoryUI>): Single<List<Long>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(word: HistoryUI): Long

  @Query("DELETE FROM histories_ui")
  fun clear(): Single<Int>

  @Query("UPDATE histories_ui SET isSelected = 0 WHERE isSelected = 1")
  fun deselectAll(): Int

  @Transaction
  fun selectWord(word: Word?): Int {
    return if (word != null) {
      deselectAll()
      add(word.toHistoryUI(true))
      1
    } else {
      deselectAll()
    }
  }
}