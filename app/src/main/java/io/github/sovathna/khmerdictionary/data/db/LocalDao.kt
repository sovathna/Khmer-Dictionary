package io.github.sovathna.khmerdictionary.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.github.sovathna.khmerdictionary.model.entity.HistoryEntity
import io.github.sovathna.khmerdictionary.model.ui.WordUi

@Dao
interface LocalDao {
    @Query("SELECT id, word FROM bookmarks WHERE word ORDER BY uid DESC LIMIT :offset, :pageSize")
    suspend fun getBookmarks(offset: Int, pageSize: Int = Const.PAGE_SIZE): List<WordUi>

    @Query("SELECT id, word FROM histories WHERE word ORDER BY uid DESC LIMIT :offset, :pageSize")
    suspend fun getHistories(offset: Int, pageSize: Int = Const.PAGE_SIZE): List<WordUi>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmarks(entities: List<BookmarkEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistories(entities: List<HistoryEntity>)
}