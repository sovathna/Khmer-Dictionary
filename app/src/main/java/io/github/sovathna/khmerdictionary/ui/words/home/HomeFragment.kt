package io.github.sovathna.khmerdictionary.ui.words.home

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsFragment

@AndroidEntryPoint
class HomeFragment : AbstractWordsFragment<HomeViewModel>() {
  override val viewModel: HomeViewModel by viewModels()
}