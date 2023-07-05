package io.github.sovathna.khmerdictionary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sovathna.khmerdictionary.model.entity.DictEntity

@Database(entities = [DictEntity::class], version = 2, exportSchema = false)
abstract class DictDb : RoomDatabase() {
  abstract fun dao(): DictDao
}