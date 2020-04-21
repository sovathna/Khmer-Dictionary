package com.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class SplashModule {

  @Provides
  fun viewModel(activity: SplashActivity, factory: ViewModelProvider.Factory) =
    ViewModelProvider(activity, factory)[SplashViewModel::class.java]

  @Provides
  fun checkDatabaseIntent() = PublishSubject.create<SplashIntent.CheckDatabase>()

}