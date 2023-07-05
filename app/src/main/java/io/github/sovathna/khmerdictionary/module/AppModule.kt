package io.github.sovathna.khmerdictionary.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.startup.AppInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.sovathna.khmerdictionary.data.db.DictDao
import io.github.sovathna.khmerdictionary.data.db.DictDb
import io.github.sovathna.khmerdictionary.data.db.LocalDao
import io.github.sovathna.khmerdictionary.data.db.LocalDb
import io.github.sovathna.khmerdictionary.initializer.DictDbInitializer
import io.github.sovathna.khmerdictionary.initializer.LocalDbInitializer
import io.github.sovathna.khmerdictionary.initializer.StoreInitializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun dictDb(initializer: AppInitializer): DictDb =
        initializer.initializeComponent(DictDbInitializer::class.java)

    @Provides
    @Singleton
    fun dictDao(db: DictDb): DictDao = db.dao()

    @Provides
    @Singleton
    fun localDb(initializer: AppInitializer): LocalDb =
        initializer.initializeComponent(LocalDbInitializer::class.java)

    @Provides
    @Singleton
    fun localDao(db: LocalDb): LocalDao = db.dao()

    @Provides
    @Singleton
    fun store(initializer: AppInitializer): DataStore<Preferences> =
        initializer.initializeComponent(StoreInitializer::class.java)

    @Provides
    @Singleton
    fun initializer(@ApplicationContext context: Context): AppInitializer =
        AppInitializer.getInstance(context)

}

val Context.store: DataStore<Preferences> by preferencesDataStore(name = "settings")