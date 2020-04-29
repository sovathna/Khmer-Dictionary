package com.sovathna.khmerdictionary.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sovathna.khmerdictionary.domain.model.WordEntity
import io.reactivex.Single

@Dao
interface WordDao {
  @Query("SELECT * FROM dict LIMIT :offset, :pageSize")
  fun getWordList(offset: Int, pageSize: Int): Single<List<WordEntity>>

  @Query("SELECT * FROM dict WHERE word LIKE :filter LIMIT :offset, :pageSize")
  fun getFilterWordList(filter: String, offset: Int, pageSize: Int): Single<List<WordEntity>>

  @Query("SELECT * FROM dict WHERE id = :id")
  fun getDefinition(id: Long): Single<WordEntity>

  @Insert
  fun inserts(words: List<WordEntity>): Single<List<Long>>

}