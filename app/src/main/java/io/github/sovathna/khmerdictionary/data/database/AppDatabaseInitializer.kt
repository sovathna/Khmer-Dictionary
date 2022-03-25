package io.github.sovathna.khmerdictionary.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase

class AppDatabaseInitializer : Initializer<AppDatabase> {
    companion object {
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE dict ADD COLUMN selected INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE dict ADD COLUMN favorite INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE dict ADD COLUMN history INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE dict ADD COLUMN timestamp INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }

    override fun create(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "dict.db")
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf()
}