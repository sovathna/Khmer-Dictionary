package io.github.sovathna.khmerdictionary.ui.main

import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

  @Provides
  @ActivityScoped
  fun recycledViewPool(): RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
}