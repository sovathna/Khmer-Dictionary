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
    val id: Long = 0
) {
    fun toHomeEntity() = HomeEntity(word = word, definition = definition, id = id)
}