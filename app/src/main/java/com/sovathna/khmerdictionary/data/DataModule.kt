package com.sovathna.khmerdictionary.data

import dagger.Module

@Module(
  includes = [
    RepositoryModule::class,
    LocalModule::class,
    InteractorModule::class,
    RemoteModule::class
  ]
)
class DataModule