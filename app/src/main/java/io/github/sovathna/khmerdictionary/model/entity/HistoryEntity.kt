package io.github.sovathna.khmerdictionary.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "histories", indices = [Index("id", unique = true)])
data class HistoryEntity(
  @ColumnInfo("id")
  val id: Long,
  @ColumnInfo("word")
  val word: String,
  @PrimaryKey(autoGenerate = true)
  val uid: Long = 0L
)