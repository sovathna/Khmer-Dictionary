package io.github.sovathna.khmerdictionary.data.interactors

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.sovathna.khmerdictionary.config.Const
import io.github.sovathna.khmerdictionary.data.SettingsDataSource
import io.github.sovathna.khmerdictionary.domain.SplashService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipInputStream
import javax.inject.Inject

@ViewModelScoped
class DownloadInteractor @Inject constructor(
  private val service: SplashService,
  private val ioDispatcher: CoroutineDispatcher,
  private val settings: SettingsDataSource,
  @ApplicationContext private val context: Context
) {

  companion object {
    private const val BYTES_TO_MB = 1_000_000.0
  }

  sealed interface Result {
    data class Downloading(val read: Double, val size: Double) : Result
    object Done : Result
    data class Error(val error: Throwable) : Result
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  fun downloadFlow(): Flow<Result> {

    val dbFile = context.getDatabasePath(Const.DB_NAME)
    var dbOutStream: FileOutputStream? = null
    var inStream: InputStream? = null
    var zipInStream: ZipInputStream? = null
    return flow {
      try {
        if (dbFile.exists() && settings.isDownloaded()) {
          emit(Result.Downloading(1.0, 1.0))
        } else {
          emit(Result.Downloading(0.0, 0.0))
          dbFile?.parentFile?.let { if (!it.exists()) it.mkdirs() }
          dbOutStream = FileOutputStream(dbFile)
          inStream = service.downloadDatabase(Const.DB_URL).byteStream()
          zipInStream = ZipInputStream(inStream)
          val entry = zipInStream!!.nextEntry
          val reader = ByteArray(4096)
          var totalRead = 0L
          var last = System.currentTimeMillis()
          val size = entry.compressedSize / BYTES_TO_MB
          val scale = entry.size.toDouble() / entry.compressedSize
          while (true) {
            currentCoroutineContext().ensureActive()
            val read = zipInStream!!.read(reader)
            if (read == -1) break
            dbOutStream!!.write(reader, 0, read)
            totalRead += read
            val current = System.currentTimeMillis()
            if (last + 500 <= current) {
              val tmp = totalRead / scale / BYTES_TO_MB
              Timber.d("downloading: $tmp/$size")
              emit(Result.Downloading(tmp, size))
              last = current
            }
          }
          dbOutStream!!.flush()
          Timber.d("download done: $size")
          emit(Result.Downloading(size, size))
          settings.downloaded()
        }
        emit(Result.Done)
      } finally {
        zipInStream?.closeEntry()
        zipInStream?.close()
        inStream?.close()
        dbOutStream?.close()
      }
    }.flowOn(ioDispatcher)
      .catch {
        Timber.e(it)
        dbFile?.delete()
        emit(Result.Error(it))
      }
  }


}