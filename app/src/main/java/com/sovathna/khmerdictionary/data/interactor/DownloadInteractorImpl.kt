package com.sovathna.khmerdictionary.data.interactor

import android.content.Context
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.interactor.DownloadInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import com.sovathna.khmerdictionary.domain.model.result.DownloadResult
import com.sovathna.khmerdictionary.domain.service.DownloadService
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit
import java.util.zip.ZipInputStream
import javax.inject.Inject

class DownloadInteractorImpl @Inject constructor(
  private val service: DownloadService,
  private val context: Context
) : DownloadInteractor() {

  override val download = ObservableTransformer<DownloadIntent.Download, DownloadResult> {
    it.flatMap {

      service.download(Const.RAW_DB_URL)
        .subscribeOn(Schedulers.io())
        .delaySubscription(1, TimeUnit.SECONDS)
        .flatMap(::saveZip)
        .doOnError(Timber::e)
        .startWith(DownloadResult.Progress(0, 0))
        .onErrorReturn(DownloadResult::Fail)
    }
  }

  override val progress = ObservableTransformer<DownloadIntent.Progress, DownloadResult> {
    it.map { i -> DownloadResult.Progress(i.download, i.total) }
      .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
      .cast(DownloadResult::class.java)
  }

  private fun saveZip(
    body: ResponseBody
  ): Observable<DownloadResult> {

    val tmp = context.getFileStreamPath("db_tmp.zip")
    if (tmp.exists()) tmp.delete()

    val file = context.getDatabasePath(Const.DB_NAME)

    return Observable.create<DownloadResult> { emitter ->

      var tmpInStream: InputStream? = null
      var tmpOutStream: OutputStream? = null

      var inStream: ZipInputStream? = null
      var outStream: OutputStream? = null
      try {
        val tmpReader = ByteArray(4096)
        tmpInStream = body.byteStream()
        tmpOutStream = FileOutputStream(tmp)
        var tmpTotalRead = 0L
        while (!emitter.isDisposed) {
          val read = tmpInStream.read(tmpReader)
          if (read == -1) break
          tmpOutStream.write(tmpReader, 0, read)
          tmpTotalRead += read
//        LogUtil.i("save $tmpTotalRead")
          emitter.onNext(DownloadResult.Progress(tmpTotalRead, body.contentLength()))
        }


        inStream = ZipInputStream(tmp.inputStream())

        inStream.nextEntry?.let {
          outStream = FileOutputStream(file)
          var totalRead = 0L
          while (!emitter.isDisposed) {
            val read = inStream.read(tmpReader)
            if (read == -1) break
            outStream?.write(tmpReader, 0, read)
            totalRead += read
//            LogUtil.i("extract $totalRead ${it.size}")
            emitter.onNext(DownloadResult.Progress(totalRead, it.size, true))
          }
          inStream.closeEntry()
          outStream?.flush()
        }

        tmp.delete()
        emitter.onNext(DownloadResult.Success)

      } catch (e: Exception) {
        emitter.tryOnError(e)

      } finally {
        tmpInStream?.close()
        tmpOutStream?.close()
        inStream?.close()
        outStream?.close()
        emitter.onComplete()
      }

    }.doOnDispose {
      if (tmp.exists()) tmp.delete()
      if (file.exists()) file.delete()
    }.subscribeOn(Schedulers.io())
  }

}