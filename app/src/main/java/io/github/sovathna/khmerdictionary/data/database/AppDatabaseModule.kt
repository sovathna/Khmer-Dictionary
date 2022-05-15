package io.github.sovathna.khmerdictionary.data.database

import androidx.startup.AppInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {

  @Provides
  @Singleton
  fun appDatabase(initializer: AppInitializer): AppDatabase =
    initializer.initializeComponent(AppDatabaseInitializer::class.java)
}