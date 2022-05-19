package io.github.sovathna.khmerdictionary.domain.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

  @Query("SELECT * FROM dict WHERE word LIKE :filter ORDER BY word ASC")
  fun filteredWords(filter: String): PagingSource<Int, WordEntity>

  @Query("SELECT * FROM dict WHERE is_bookmark=1 AND word LIKE :filter ORDER BY word ASC")
  fun filteredBookmarks(filter: String): PagingSource<Int, WordEntity>

  @Query("SELECT * FROM dict WHERE is_history=1 AND word LIKE :filter ORDER BY timestamp DESC")
  fun filteredHistories(filter: String): PagingSource<Int, WordEntity>

  @Query("UPDATE dict SET is_bookmark=:isBookmark WHERE id=:id")
  suspend fun updateBookmark(id: Long, isBookmark: Boolean)

  @Query("UPDATE dict SET is_history=1, timestamp=:timestamp WHERE id=:id")
  suspend fun updateHistory(id: Long, timestamp: Long = System.currentTimeMillis())

  @Query("SELECT * FROM dict WHERE is_select=1")
  fun observeWord(): Flow<WordEntity?>

  @Query("UPDATE dict SET is_select=1 WHERE id=:id")
  suspend fun updateSelection(id: Long)

  @Query("UPDATE dict SET is_select=0 WHERE is_select=1")
  suspend fun clearSelection()

  @Query("UPDATE dict SET is_history=0 WHERE is_history=1")
  suspend fun clearHistory()

  @Query("UPDATE dict SET is_bookmark=0 WHERE is_bookmark=1")
  suspend fun clearBookmark()

}