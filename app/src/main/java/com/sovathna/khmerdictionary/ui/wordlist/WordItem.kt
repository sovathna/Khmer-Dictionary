package com.sovathna.khmerdictionary.ui.wordlist

import androidx.recyclerview.widget.DiffUtil
import com.sovathna.khmerdictionary.domain.model.Word

data class WordItem(
  val word: Word,
  val isSelected: Boolean = false
) {
  companion object {
    val ITEM_CALLBACK = object : DiffUtil.ItemCallback<WordItem>() {
      override fun areItemsTheSame(oldItem: WordItem, newItem: WordItem) =
        oldItem.word.id == newItem.word.id

      override fun areContentsTheSame(oldItem: WordItem, newItem: WordItem) =
        oldItem == newItem
    }
  }
}