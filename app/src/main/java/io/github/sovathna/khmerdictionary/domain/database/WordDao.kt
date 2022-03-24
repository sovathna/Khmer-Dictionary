package io.github.sovathna.khmerdictionary.domain.database

import androidx.room.Dao
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.WordEntity

@Dao
interface WordDao {

  @Query("SELECT * FROM dict ORDER BY word LIMIT :offset, :pageSize")
  suspend fun words(offset: Int, pageSize: Int): List<WordEntity>

}