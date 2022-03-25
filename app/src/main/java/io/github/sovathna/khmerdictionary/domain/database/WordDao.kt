package io.github.sovathna.khmerdictionary.domain.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.WordEntity

@Dao
interface WordDao {

    @Query("SELECT * FROM dict ORDER BY word")
    fun homeWords(): PagingSource<Int, WordEntity>

    @Query("SELECT * FROM dict WHERE word LIKE :filter")
    fun filteredWords(filter:String):PagingSource<Int,WordEntity>

}