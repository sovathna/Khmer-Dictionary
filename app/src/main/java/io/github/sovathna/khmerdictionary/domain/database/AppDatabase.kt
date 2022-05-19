package io.github.sovathna.khmerdictionary.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sovathna.khmerdictionary.model.WordEntity

@Database(
  entities = [
    WordEntity::class
  ],
  version = 3,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun wordDao(): WordDao

}