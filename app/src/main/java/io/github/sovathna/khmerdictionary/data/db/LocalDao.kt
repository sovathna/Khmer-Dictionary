package io.github.sovathna.khmerdictionary.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.github.sovathna.khmerdictionary.model.entity.HistoryEntity
import io.github.sovathna.khmerdictionary.model.ui.WordUi

@Dao
interface LocalDao {
  @Query("SELECT id, word FROM bookmarks ORDER BY uid DESC")
  suspend fun getBookmarks(): List<WordUi>

  @Query("SELECT id, word FROM histories ORDER BY uid DESC")
  suspend fun getHistories(): List<WordUi>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addBookmark(entity: BookmarkEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addHistory(entity: HistoryEntity)

  @Query("SELECT * FROM bookmarks WHERE id = :id")
  suspend fun getBookmark(id: Long): BookmarkEntity?

  @Query("DELETE FROM bookmarks WHERE id = :id")
  suspend fun deleteBookmark(id: Long)
}