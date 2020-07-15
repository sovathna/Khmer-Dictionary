package com.sovathna.khmerdictionary.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.ui.words.WordItem

@Entity(tableName = "searches_ui")
data class SearchUI(
  @PrimaryKey
  val id: Long,
  val name: String,
  val isSelected: Boolean = false
) {
  fun toWordItem() = WordItem(Word(id, name), isSelected)
}