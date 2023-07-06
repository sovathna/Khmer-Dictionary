package io.github.sovathna.khmerdictionary.ui.main

import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object MainFragmentModule {

  @Provides
  @Reusable
  fun recycledViewPool(): RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
}