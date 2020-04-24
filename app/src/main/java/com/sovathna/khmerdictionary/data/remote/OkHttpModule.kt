package com.sovathna.khmerdictionary.data.remote

import com.sovathna.khmerdictionary.domain.model.DownloadResponseBody
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

@Module
class OkHttpModule {

  @Provides
  fun downloadOkHttp(progressIntent: PublishSubject<DownloadIntent.Progress>) = OkHttpClient()
    .newBuilder()
    .addInterceptor(object : Interceptor {
      override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.proceed(chain.request())
        if (origin.body != null)
          return origin.newBuilder().body(
            DownloadResponseBody(
              origin.body!!,
              progressIntent
            )
          ).build()
        return origin
      }
    })
    .build()

}