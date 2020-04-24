package com.sovathna.khmerdictionary.domain.model

import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import okio.*

class DownloadResponseBody(
  private val body: ResponseBody,
  private val progressIntent: PublishSubject<DownloadIntent.Progress>
) : ResponseBody() {

  private var bufferedSource: BufferedSource? = null

  override fun contentLength() = body.contentLength()

  override fun contentType() = body.contentType()

  override fun source(): BufferedSource {
    if (bufferedSource == null)
      bufferedSource = getSource(body.source()).buffer()
    return bufferedSource!!
  }

  private fun getSource(source: Source): Source {
    var totalRead = 0L
    return object : ForwardingSource(source) {
      override fun read(sink: Buffer, byteCount: Long): Long {
        val read = super.read(sink, byteCount)
        if (read != -1L) totalRead += read
        progressIntent.onNext(DownloadIntent.Progress(totalRead, body.contentLength()))
        return read
      }
    }
  }
}