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
    val id: Long = 0,
    @ColumnInfo(name = "selected")
    val selected: Boolean = false,
    @ColumnInfo(name = "favorite")
    val favorite: Boolean = false,
    @ColumnInfo(name = "history")
    val history: Boolean = false,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0L,
)