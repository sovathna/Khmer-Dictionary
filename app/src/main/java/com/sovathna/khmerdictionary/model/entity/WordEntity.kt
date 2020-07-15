package com.sovathna.khmerdictionary.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sovathna.khmerdictionary.model.Word

@Entity(tableName = "dict")
data class WordEntity(
  @ColumnInfo(name = "word")
  val word: String,
  @ColumnInfo(name = "definition")
  val definition: String,
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Long = 0
) {
  fun toWord() = Word(id, word)
  fun toWordUI() = WordUI(id, word)
  fun toSearchUI() = SearchUI(id, word)
}