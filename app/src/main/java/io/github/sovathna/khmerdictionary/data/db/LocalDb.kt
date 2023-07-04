package io.github.sovathna.khmerdictionary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sovathna.khmerdictionary.model.entity.BookmarkEntity
import io.github.sovathna.khmerdictionary.model.entity.HistoryEntity
import io.github.sovathna.khmerdictionary.model.entity.SearchEntity
import io.github.sovathna.khmerdictionary.model.entity.WordEntity

@Database(
    entities = [
        WordEntity::class,
        SearchEntity::class,
        BookmarkEntity::class,
        HistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDb : RoomDatabase() {
    abstract fun dao(): LocalDao
}