package io.github.sovathna.khmerdictionary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home")
data class HomeEntity(
    @ColumnInfo(name = "word")
    val word: String,
    @ColumnInfo(name = "definition")
    val definition: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "selected")
    val selected: Boolean = false,
)