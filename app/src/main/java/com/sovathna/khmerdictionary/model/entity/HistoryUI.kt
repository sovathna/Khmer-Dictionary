package com.sovathna.khmerdictionary.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.ui.words.WordItem

@Entity(tableName = "histories_ui", indices = [Index(value = ["id"], unique = true)])
data class HistoryUI(
  val id: Long,
  val name: String,
  val isSelected: Boolean = false,
  @PrimaryKey(autoGenerate = true)
  val uid: Long = 0L
) {
  fun toWordItem() = WordItem(Word(id, name), isSelected)
}