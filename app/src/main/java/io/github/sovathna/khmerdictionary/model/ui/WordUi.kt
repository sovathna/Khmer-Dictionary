package io.github.sovathna.khmerdictionary.model.ui

import androidx.room.ColumnInfo
import io.github.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.github.sovathna.khmerdictionary.model.entity.HistoryEntity
import io.github.sovathna.khmerdictionary.model.entity.SearchEntity
import io.github.sovathna.khmerdictionary.model.entity.WordEntity

data class WordUi(
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("word")
    val word: String
) {
    fun toWordEntity() = WordEntity(id, word)

    fun toSearchEntity() = SearchEntity(id, word)

    fun toBookmarkEntity() = BookmarkEntity(id, word)

    fun toHistoryEntity() = HistoryEntity(id, word)
}