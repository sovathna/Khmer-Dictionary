package io.github.sovathna.khmerdictionary.initializer

import android.content.Context
import androidx.room.Room
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.data.db.LocalDb

class LocalDbInitializer : Initializer<LocalDb> {
    override fun create(context: Context): LocalDb {
        return Room.databaseBuilder(context, LocalDb::class.java, Const.LOCAL_DB_NAME).build()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}