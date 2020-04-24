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
import java.util.zip.ZipInputStream
import javax.inject.Inject

class DownloadInteractorImpl @Inject constructor(
  private val service: DownloadService,
  private val context: Context
) : DownloadInteractor() {

  override val download = ObservableTransformer<DownloadIntent.Download, DownloadResult> {
    it.flatMap {

      service.download(Const.RAW_DB_URL)
        .flatMap(::unzipAndSave)
        .doOnError(Timber::e)
        .startWith(DownloadResult.Progress)
        .onErrorReturn(DownloadResult::Fail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }
  }

  override val progress = ObservableTransformer<DownloadIntent.Progress, DownloadResult> {
    it.map { i -> DownloadResult.DownloadProgress(i.download, i.total) }
      .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
      .cast(DownloadResult::class.java)
  }

  private fun unzipAndSave(
    body: ResponseBody
  ): Observable<DownloadResult> {
    return Observable.fromCallable {
      val file = context.getDatabasePath(Const.DB_NAME)
      val reader = ByteArray(1024)

      val zInStream = ZipInputStream(body.byteStream())
      zInStream.nextEntry.let {
        val outStream = FileOutputStream(file)
        while (true) {
          val read = zInStream.read(reader)
          if (read == -1) break
          outStream.write(reader, 0, read)
        }
        outStream.flush()
        outStream.close()
      }
      zInStream.closeEntry()
      zInStream.close()
      DownloadResult.Success
    }.cast(DownloadResult::class.java)
      .startWith(DownloadResult.Saving)

  }

}