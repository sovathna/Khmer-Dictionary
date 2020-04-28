package com.sovathna.khmerdictionary.data.interactor

import android.content.Context
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.result.SplashResult
import com.sovathna.khmerdictionary.domain.service.DownloadService
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipInputStream
import javax.inject.Inject

class SplashInteractorImpl @Inject constructor(
  private val downloadService: DownloadService,
  private val context: Context
) : SplashInteractor() {

  override val checkDatabase = ObservableTransformer<SplashIntent.CheckDatabase, SplashResult> {
    it.flatMap {
      Observable.just(context.getDatabasePath(Const.DB_NAME).exists())
        .flatMap { exists ->
          if (exists) Observable.just(SplashResult.Success)
          else downloadService.download(Const.RAW_DB_URL)
            .subscribeOn(Schedulers.io())
            .flatMap(::saveZip)
            .startWith(SplashResult.Downloading(0, 0))
        }
        .onErrorReturn(SplashResult::Fail)
        .startWith(SplashResult.Progressing)
    }
  }

  private fun saveZip(
    body: ResponseBody
  ): Observable<SplashResult> {

    val tmp = context.getFileStreamPath("db_tmp.zip")
    if (tmp.exists()) tmp.delete()

    val file = context.getDatabasePath(Const.DB_NAME)

    return Observable.create<SplashResult> { emitter ->

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
          emitter.onNext(SplashResult.Downloading(tmpTotalRead, body.contentLength()))
        }

        inStream = ZipInputStream(tmp.inputStream())

        inStream.nextEntry?.let {
          emitter.onNext(SplashResult.Downloading(it.size, it.size))
          outStream = FileOutputStream(file)
          while (!emitter.isDisposed) {
            val read = inStream.read(tmpReader)
            if (read == -1) break
            outStream?.write(tmpReader, 0, read)
          }
          inStream.closeEntry()
          outStream?.flush()
        }

        tmp.delete()
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
      if (tmp.exists()) tmp.delete()
      if (file.exists()) file.delete()
    }
      .doOnDispose {
        if (tmp.exists()) tmp.delete()
        if (file.exists()) file.delete()
      }.subscribeOn(Schedulers.io())
  }
}