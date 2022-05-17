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

  @Query("SELECT * FROM dict WHERE is_bookmark=:isBookmark AND word LIKE :filter ORDER BY word ASC")
  fun filteredBookmarks(filter: String, isBookmark: Boolean = true): PagingSource<Int, WordEntity>

  @Query("SELECT * FROM dict WHERE is_history=:isHistory AND word LIKE :filter ORDER BY timestamp DESC")
  fun filteredHistories(filter: String, isHistory: Boolean = true): PagingSource<Int, WordEntity>

  @Query("UPDATE dict SET is_bookmark=:isBookmark WHERE id=:id")
  suspend fun updateBookmark(id: Long, isBookmark: Boolean)

  @Query("UPDATE dict SET is_history=:isHistory, timestamp=:timestamp WHERE id=:id")
  suspend fun updateHistory(
    id: Long,
    isHistory: Boolean = true,
    timestamp: Long = System.currentTimeMillis()
  )

  @Query("SELECT * FROM dict WHERE is_select=:isSelect")
  fun observeWord(isSelect: Boolean = true): Flow<WordEntity?>

  @Query("UPDATE dict SET is_select=:isSelect WHERE id=:id")
  suspend fun updateSelection(id: Long, isSelect: Boolean)

  @Query("UPDATE dict SET is_history=0 WHERE is_history=1")
  suspend fun clearHistory()

  @Query("UPDATE dict SET is_bookmark=0 WHERE is_bookmark=1")
  suspend fun clearBookmark()

}