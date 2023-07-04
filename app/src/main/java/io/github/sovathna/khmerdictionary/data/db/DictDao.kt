package io.github.sovathna.khmerdictionary.data.db

import androidx.room.Dao
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.ui.WordUi

@Dao
interface DictDao {
    @Query("SELECT id, word FROM dict WHERE word LIKE :filter ORDER BY word ASC LIMIT :offset, :pageSize")
    suspend fun getWords(filter: String, offset: Int, pageSize: Int = 100): List<WordUi>
}