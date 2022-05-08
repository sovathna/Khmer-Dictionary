package io.github.sovathna.khmerdictionary.domain.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.WordEntity

@Dao
interface WordDao {

  @Query("SELECT * FROM dict WHERE word LIKE :filter ORDER BY word")
  fun filteredWords(filter: String): PagingSource<Int, WordEntity>

  @Query("SELECT * FROM dict WHERE favorite=:favorite AND word LIKE :filter ORDER BY word")
  fun filteredBookmarks(filter: String, favorite: Boolean = true): PagingSource<Int, WordEntity>

  @Query("SELECT * FROM dict WHERE history=:history AND word LIKE :filter ORDER BY timestamp DESC")
  fun filteredHistories(filter: String, history: Boolean = true): PagingSource<Int, WordEntity>

  @Query("UPDATE dict SET favorite=:favorite WHERE id=:id")
  suspend fun updateBookmark(id: Long, favorite: Boolean)

  @Query("UPDATE dict SET history=:history, timestamp=:timestamp WHERE id=:id")
  suspend fun updateHistory(
    id: Long,
    history: Boolean = true,
    timestamp: Long = System.currentTimeMillis()
  )

}