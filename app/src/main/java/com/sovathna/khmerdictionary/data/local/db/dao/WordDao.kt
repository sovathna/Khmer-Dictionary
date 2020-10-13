package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.sovathna.khmerdictionary.model.entity.WordEntity
import io.reactivex.Single

@Dao
interface WordDao {
  @Query("SELECT * FROM dict ORDER BY word LIMIT :offset, :pageSize")
  fun get(
    offset: Int,
    pageSize: Int
  ): Single<List<WordEntity>>

  @Query("SELECT * FROM dict WHERE word LIKE :filter ORDER BY word LIMIT :offset, :pageSize")
  fun search(
    filter: String,
    offset: Int,
    pageSize: Int
  ): Single<List<WordEntity>>

  @Query("SELECT * FROM dict WHERE id = :id")
  fun get(
    id: Long
  ): Single<WordEntity>

//  @Insert
//  fun inserts(
//    words: List<WordEntity>
//  ): Single<List<Long>>

}