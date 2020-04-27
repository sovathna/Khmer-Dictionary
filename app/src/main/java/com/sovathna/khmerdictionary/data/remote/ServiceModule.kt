package com.sovathna.khmerdictionary.data.remote

import com.sovathna.khmerdictionary.domain.service.DownloadService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ServiceModule {

    @Provides
    fun downloadService(retrofit: Retrofit): DownloadService =
        retrofit.create(DownloadService::class.java)
}