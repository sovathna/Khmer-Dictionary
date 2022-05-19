package io.github.sovathna.khmerdictionary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dict")
data class WordEntity(
  @ColumnInfo(name = "word")
  val word: String,
  @ColumnInfo(name = "definition")
  val definition: String,
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long = 0,
  @ColumnInfo(name = "is_select")
  val isSelect: Boolean = false,
  @ColumnInfo(name = "is_bookmark")
  val isBookmark: Boolean = false,
  @ColumnInfo(name = "is_history")
  val isHistory: Boolean = false,
  @ColumnInfo(name = "timestamp")
  val timestamp: Long = 0L,
)