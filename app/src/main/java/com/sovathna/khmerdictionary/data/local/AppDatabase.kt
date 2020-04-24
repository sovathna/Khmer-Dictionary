package com.sovathna.khmerdictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sovathna.khmerdictionary.domain.dao.WordDao
import com.sovathna.khmerdictionary.domain.model.WordEntity

@Database(entities = [WordEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun wordDao(): WordDao
}