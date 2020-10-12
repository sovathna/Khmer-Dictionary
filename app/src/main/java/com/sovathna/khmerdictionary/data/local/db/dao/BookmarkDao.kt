package com.sovathna.khmerdictionary.data.local.db.dao

import androidx.room.*
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.reactivex.Single

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmark ORDER BY uid DESC LIMIT :offset, :pageSize")
  fun get(
    offset: Int,
    pageSize: Int
  ): Single<List<BookmarkEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun add(
    word: BookmarkEntity
  ): Long

  @Query("SELECT * FROM bookmark WHERE id=:wordId")
  fun get(
    wordId: Long
  ): Single<BookmarkEntity>

  @Query("SELECT * FROM bookmark WHERE id=:wordId")
  fun getSync(
    wordId: Long
  ): BookmarkEntity?

  @Transaction
  fun addDelete(word: Word): Boolean {
    val entity = getSync(word.id)
    return if (entity != null) {
      delete(word.id)
      false
    } else {
      add(word.toBookmarkEntity())
      true
    }
  }

  @Query("DELETE FROM bookmark WHERE id=:wordId")
  fun delete(
    wordId: Long
  ): Int

  @Query("DELETE FROM bookmark")
  fun clear(): Single<Int>
}