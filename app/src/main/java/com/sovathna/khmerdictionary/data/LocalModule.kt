package com.sovathna.khmerdictionary.data

import android.content.Context
import androidx.room.Room
import com.sovathna.khmerdictionary.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

  @Provides
  @Singleton
  fun appDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, "dict.db")
      .createFromAsset("databases/dictdb")
      .build()

}