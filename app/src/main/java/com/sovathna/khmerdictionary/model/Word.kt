package com.sovathna.khmerdictionary.model

import android.os.Parcelable
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
import com.sovathna.khmerdictionary.model.entity.BookmarkUI
import com.sovathna.khmerdictionary.model.entity.HistoryEntity
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word(
  val id: Long,
  val name: String
) : Parcelable {
  fun toHistoryEntity() = HistoryEntity(id, name)
  fun toHistoryUI(isSelected: Boolean = false) = HistoryUI(id, name, isSelected)
  fun toBookmarkEntity() = BookmarkEntity(id, name)
  fun toBookmarkUI(isSelected: Boolean = false) = BookmarkUI(id, name, isSelected)
}