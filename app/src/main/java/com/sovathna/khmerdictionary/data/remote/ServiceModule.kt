package com.sovathna.khmerdictionary.data.remote

import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import com.sovathna.khmerdictionary.domain.service.DownloadService
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {

  @Provides
  @Singleton
  fun progressIntent() = PublishSubject.create<DownloadIntent.Progress>()

  @Provides
  fun downloadService(retrofit: Retrofit): DownloadService =
    retrofit.create(DownloadService::class.java)
}