package io.github.sovathna.khmerdictionary.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.sovathna.khmerdictionary.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val appSettings: AppSettings,
    @ApplicationContext private val context: Context
) {

    suspend fun isDbExists(): Boolean =
        withContext(Dispatchers.IO) { context.getDatabasePath(Const.DB_NAME).exists() }

    suspend fun shouldDownloadDb(): Boolean = withContext(Dispatchers.IO) {
        val localVersion = appSettings.getDbVersion()
        val remoteVersion = Const.config.version
        val isExists = isDbExists()
        localVersion != remoteVersion || !isExists
    }

    suspend fun setDbVersion(version: Int) = appSettings.setDbVersion(version)

}