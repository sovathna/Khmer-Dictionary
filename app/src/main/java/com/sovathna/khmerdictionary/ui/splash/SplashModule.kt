package com.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
class SplashModule {

  @Provides
  fun viewModel(activity: SplashActivity, factory: ViewModelProvider.Factory) =
    ViewModelProvider(activity, factory)[SplashViewModel::class.java]

  @Provides
  fun checkDatabaseIntent() = PublishSubject.create<SplashIntent.CheckDatabase>()

  @Provides
  fun okHttp() = OkHttpClient()
    .newBuilder()
    .build()

  @Provides
  fun baseUrl() = "https://www.example.com/"

  @Provides
  fun callAdapter(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

  @Provides
  fun retrofit(baseUrl: String, client: OkHttpClient, callAdapter: CallAdapter.Factory): Retrofit =
    Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(client)
      .addCallAdapterFactory(callAdapter)
      .build()

}