package io.github.sovathna.khmerdictionary.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dict")
data class DictEntity(
  @ColumnInfo(name = "word")
  val word: String,
  @ColumnInfo(name = "definition")
  val definition: String,
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long = 0L
)