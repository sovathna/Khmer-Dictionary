package com.sovathna.khmerdictionary.data.repository

import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun appRepository(
    impl: AppRepositoryImpl
  ): AppRepository

}