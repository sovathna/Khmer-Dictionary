package io.github.sovathna.khmerdictionary.model.ui

import androidx.room.ColumnInfo
import io.github.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.github.sovathna.khmerdictionary.model.entity.HistoryEntity

data class WordUi(
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("word")
    val word: String
) {
    fun toBookmarkEntity() = BookmarkEntity(id, word)

    fun toHistoryEntity() = HistoryEntity(id, word)
}