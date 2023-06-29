package io.github.sovathna.khmerdictionary.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.sovathna.khmerdictionary.data.DownloadService
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
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class DownloadInteractor @Inject constructor(
    private val service: DownloadService,
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val BYTES_TO_MB = 1_000_000.0
    }

    sealed interface Result {
        data class Downloading(val read: Double, val size: Double) : Result
        data class Error(val error: Throwable) : Result
        data class Done(val file: File) : Result
    }

    operator fun invoke(url: String) = flow {
        var inStream: InputStream? = null
        var outStream: OutputStream? = null
        val tempFile = File(context.cacheDir, "temp_db.zip")
        try {
            emit(Result.Downloading(0.0, 0.0))
            delay(1000)
            outStream = FileOutputStream(tempFile)
            val res = service.downloadFile(url)
            inStream = res.byteStream()

            val reader = ByteArray(4096)
            var totalRead = 0L
            var last = System.currentTimeMillis()
            val size = res.contentLength() / BYTES_TO_MB
            while (true) {
                currentCoroutineContext().ensureActive()
                val read = inStream.read(reader)
                if (read == -1) break
                outStream.write(reader, 0, read)
                totalRead += read
                val current = System.currentTimeMillis()
                if (last + 500 <= current) {
                    val tmp = totalRead / BYTES_TO_MB
                    last = current
                    Timber.d("downloading: $tmp/$size")
                    emit(Result.Downloading(tmp, size))
                }
            }
            outStream.flush()
            emit(Result.Done(tempFile))
        } catch (e: Exception) {
            Timber.e(e)
            tempFile.delete()
            emit(Result.Error(e))
        } finally {
            inStream?.close()
            outStream?.close()
        }
    }.flowOn(Dispatchers.IO)
        .cancellable()

}