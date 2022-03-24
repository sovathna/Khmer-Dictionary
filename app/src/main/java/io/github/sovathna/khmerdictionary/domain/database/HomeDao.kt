package io.github.sovathna.khmerdictionary.domain.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sovathna.khmerdictionary.model.HomeEntity

@Dao
interface HomeDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun addAll(data: List<HomeEntity>)

  @Query("SELECT * FROM home")
  fun words(): PagingSource<Int, HomeEntity>

  @Query("SELECT COUNT(id) FROM home")
  suspend fun count(): Int

}