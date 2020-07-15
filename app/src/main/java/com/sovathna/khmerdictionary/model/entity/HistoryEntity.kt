package com.sovathna.khmerdictionary.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sovathna.khmerdictionary.model.Word

@Entity(tableName = "history", indices = [Index(value = ["id"], unique = true)])
data class HistoryEntity(
  @ColumnInfo(name = "id")
  val id: Long,
  @ColumnInfo(name = "word")
  val word: String,
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "uid")
  val uid: Long = 0
) {
  fun toWord() = Word(id, word)
  fun toHistoryUI() = HistoryUI(id, word, uid = uid)
}