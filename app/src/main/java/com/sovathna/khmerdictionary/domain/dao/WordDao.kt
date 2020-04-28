package com.sovathna.khmerdictionary.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.model.WordEntity
import io.reactivex.Single

@Dao
interface WordDao {
  @Query("SELECT * FROM dict LIMIT :offset, ${Const.PAGE_SIZE}")
  fun getWordList(offset: Int): Single<List<WordEntity>>

  @Query("SELECT * FROM dict WHERE word LIKE :filter LIMIT :offset, ${Const.PAGE_SIZE}")
  fun getFilterWordList(filter: String, offset: Int): Single<List<WordEntity>>

  @Query("SELECT * FROM dict WHERE id = :id")
  fun getDefinition(id: Long): Single<WordEntity>

  @Insert
  fun inserts(words: List<WordEntity>): Single<List<Long>>

}