package io.github.sovathna.khmerdictionary.app

import android.content.Context
import androidx.startup.AppInitializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun initializer(@ApplicationContext context: Context) = AppInitializer.getInstance(context)

    @Provides
    @Singleton
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

}