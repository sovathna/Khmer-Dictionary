package com.sovathna.khmerdictionary.ui.download

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class DownloadModule {

  @Provides
  fun viewModel(fragment: DownloadFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[DownloadViewModel::class.java]

  @Provides
  fun downloadIntent() = PublishSubject.create<DownloadIntent.Download>()

}