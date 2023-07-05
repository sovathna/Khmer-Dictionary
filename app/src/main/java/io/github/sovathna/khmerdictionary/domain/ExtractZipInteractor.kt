package io.github.sovathna.khmerdictionary.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.sovathna.khmerdictionary.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.zip.ZipInputStream
import javax.inject.Inject

class ExtractZipInteractor @Inject constructor(
  @ApplicationContext private val context: Context
) {

  companion object {
    private const val BYTES_TO_MB = 1_000_000.0
  }

  sealed interface Result {
    data class Extracting(val read: Double, val size: Double) : Result
    data class Error(val error: Throwable) : Result
    data class Done(val file: File) : Result
  }

  operator fun invoke(file: File) = flow {
    var zipInStream: ZipInputStream? = null
    var outStream: OutputStream? = null
    val tempFile = context.getDatabasePath(Const.DB_NAME)
    try {
      emit(Result.Extracting(0.0, 0.0))
      delay(1000)
      outStream = FileOutputStream(tempFile)

      zipInStream = ZipInputStream(file.inputStream())
      val entry = zipInStream.nextEntry
      val reader = ByteArray(4096)
      var totalRead = 0L
      var last = System.currentTimeMillis()
      val size = entry.compressedSize / BYTES_TO_MB
      val scale = entry.size.toDouble() / entry.compressedSize
      while (true) {
        currentCoroutineContext().ensureActive()
        val read = zipInStream.read(reader)
        if (read == -1) break
        outStream.write(reader, 0, read)
        totalRead += read
        val current = System.currentTimeMillis()
        if (last + 500 <= current) {
          val tmp = totalRead / scale / BYTES_TO_MB
          last = current
          Timber.d("extracting: $tmp/$size")
          emit(Result.Extracting(tmp, size))
        }
      }
      outStream.flush()
      emit(Result.Done(tempFile))
    } catch (e: Exception) {
      Timber.e(e)
      tempFile.delete()
      emit(Result.Error(e))
    } finally {
      zipInStream?.close()
      outStream?.close()
    }
  }.flowOn(Dispatchers.IO)
    .cancellable()

}