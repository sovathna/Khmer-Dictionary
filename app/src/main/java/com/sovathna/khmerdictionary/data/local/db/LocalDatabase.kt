package com.sovathna.khmerdictionary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sovathna.khmerdictionary.data.local.db.dao.*
import com.sovathna.khmerdictionary.model.entity.*

@Database(
  entities = [
    BookmarkEntity::class,
    HistoryEntity::class,
    WordUI::class,
    BookmarkUI::class,
    HistoryUI::class,
    SearchUI::class
  ],
  version = 3,
  exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun bookmarkDao(): BookmarkDao
  abstract fun historyDao(): HistoryDao
  abstract fun wordUIDao(): WordUIDao
  abstract fun searchUIDao(): SearchUIDao
  abstract fun bookmarkUIDao(): BookmarkUIDao
  abstract fun historyUIDao(): HistoryUIDao
}