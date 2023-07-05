package io.github.sovathna.khmerdictionary.data.db

import androidx.room.Dao
import androidx.room.Query
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.model.ui.WordUi

@Dao
interface DictDao {

    @Query("SELECT id, word FROM dict ORDER BY word ASC LIMIT :offset, :pageSize")
    suspend fun get(offset: Int, pageSize: Int = Const.PAGE_SIZE): List<WordUi>

    @Query("SELECT id, word FROM dict WHERE word LIKE :filter ORDER BY word ASC LIMIT :offset, :pageSize")
    suspend fun filter(filter: String, offset: Int, pageSize: Int = Const.PAGE_SIZE): List<WordUi>
}