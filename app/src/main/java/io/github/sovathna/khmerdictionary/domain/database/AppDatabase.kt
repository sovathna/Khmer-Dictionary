package io.github.sovathna.khmerdictionary.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sovathna.khmerdictionary.model.HomeEntity
import io.github.sovathna.khmerdictionary.model.WordEntity

@Database(
    entities = [
        WordEntity::class,
        HomeEntity::class
    ],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    abstract fun homeDao(): HomeDao

}