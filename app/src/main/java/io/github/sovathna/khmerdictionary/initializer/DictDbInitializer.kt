package io.github.sovathna.khmerdictionary.initializer

import android.content.Context
import androidx.room.Room
import androidx.startup.Initializer
import io.github.sovathna.khmerdictionary.Const
import io.github.sovathna.khmerdictionary.data.db.DictDb

class DictDbInitializer : Initializer<DictDb> {
    override fun create(context: Context): DictDb {
        return Room.databaseBuilder(context, DictDb::class.java, Const.DB_NAME).build()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}