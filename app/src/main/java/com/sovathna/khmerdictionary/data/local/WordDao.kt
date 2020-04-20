package com.sovathna.khmerdictionary.data.local

import androidx.room.Dao
import androidx.room.Query
import com.sovathna.khmerdictionary.Const
import io.reactivex.Single

@Dao
interface WordDao {
  @Query("SELECT * FROM dict LIMIT :offset, ${Const.PAGE_SIZE}")
  fun getWordList(offset: Int): Single<List<WordEntity>>

  @Query("SELECT * FROM dict WHERE word LIKE :filter LIMIT :offset, ${Const.PAGE_SIZE}")
  fun getFilterWordList(filter:String,offset: Int): Single<List<WordEntity>>
}