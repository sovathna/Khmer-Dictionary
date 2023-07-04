package io.github.sovathna.khmerdictionary.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.github.sovathna.khmerdictionary.model.entity.HistoryEntity
import io.github.sovathna.khmerdictionary.model.entity.SearchEntity
import io.github.sovathna.khmerdictionary.model.entity.WordEntity
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {
    @Query("SELECT id, word FROM words ORDER BY word ASC")
    fun getWords(): Flow<List<WordUi>>

    @Query("SELECT id, word FROM searches ORDER BY word ASC")
    fun getSearchWords(): Flow<List<WordUi>>

    @Query("SELECT id, word FROM bookmarks WHERE word ORDER BY uid DESC")
    fun getBookmarks(): Flow<List<WordUi>>

    @Query("SELECT id, word FROM histories WHERE word ORDER BY uid DESC")
    fun getHistories(): Flow<List<WordUi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWords(entities: List<WordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearches(entities: List<SearchEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmarks(entities: List<BookmarkEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistories(entities: List<HistoryEntity>)
}