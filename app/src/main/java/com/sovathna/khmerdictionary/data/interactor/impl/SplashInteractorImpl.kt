package com.sovathna.khmerdictionary.data.interactor.impl

import com.sovathna.androidmvi.Logger
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.data.interactor.base.SplashInteractor
import com.sovathna.khmerdictionary.data.remote.service.DownloadService
import com.sovathna.khmerdictionary.model.intent.SplashIntent
import com.sovathna.khmerdictionary.model.result.SplashResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipInputStream
import javax.inject.Inject

class SplashInteractorImpl @Inject constructor(
  private val downloadService: DownloadService
) : SplashInteractor() {

  override val checkDatabase =
    ObservableTransformer<SplashIntent.CheckDatabase, SplashResult> {
      it.flatMap { intent ->
        Observable
          .just(intent.db.exists())
          .flatMap { exists ->
            if (exists) {
              Observable.just(SplashResult.Success)
            } else {
              downloadService
                .download(Const.RAW_DB_URL)
                .doOnError { Logger.e(it) }
                .subscribeOn(Schedulers.io())
                .flatMap { response ->
                  saveZip(response, intent.db, intent.tmpDb)
                }.onErrorReturn { SplashResult.Fail(it) }
                .startWith(SplashResult.Downloading(0, 0))
            }
          }.onErrorReturn(SplashResult::Fail)
          .startWith(SplashResult.Progressing)
      }
    }

  private fun saveZip(
    body: ResponseBody,
    db: File,
    tmpDb: File
  ): Observable<SplashResult> {

    if (tmpDb.exists()) tmpDb.delete()

    return Observable.create<SplashResult> { emitter ->

      var tmpInStream: InputStream? = null
      var tmpOutStream: OutputStream? = null

      var inStream: ZipInputStream? = null
      var outStream: OutputStream? = null
      try {
        val tmpReader = ByteArray(4096)
        tmpInStream = body.byteStream()
        tmpOutStream = FileOutputStream(tmpDb)
        var tmpTotalRead = 0L
        while (!emitter.isDisposed) {
          val read = tmpInStream.read(tmpReader)
          if (read == -1) break
          tmpOutStream.write(tmpReader, 0, read)
          tmpTotalRead += read
          emitter.onNext(
            SplashResult.Downloading(tmpTotalRead, body.contentLength())
          )
        }

        inStream = ZipInputStream(tmpDb.inputStream())

        inStream.nextEntry?.let {
          emitter.onNext(SplashResult.Downloading(it.size, it.size))
          outStream = FileOutputStream(db)
          while (!emitter.isDisposed) {
            val read = inStream.read(tmpReader)
            if (read == -1) break
            outStream?.write(tmpReader, 0, read)
          }
          inStream.closeEntry()
          outStream?.flush()
        }

        tmpDb.delete()
        emitter.onNext(SplashResult.Success)

      } catch (e: Exception) {
        emitter.tryOnError(e)

      } finally {
        tmpInStream?.close()
        tmpOutStream?.close()
        inStream?.close()
        outStream?.close()
        emitter.onComplete()
      }

    }.doOnError {
      if (tmpDb.exists()) tmpDb.delete()
      if (db.exists()) db.delete()
    }.doOnDispose {
      if (tmpDb.exists()) tmpDb.delete()
      if (db.exists()) db.delete()
    }.subscribeOn(Schedulers.io())
  }
}