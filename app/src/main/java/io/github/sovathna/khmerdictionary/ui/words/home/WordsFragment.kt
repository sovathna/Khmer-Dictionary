package io.github.sovathna.khmerdictionary.ui.words.home

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsFragment

@AndroidEntryPoint
class WordsFragment : AbstractWordsFragment() {

  override val viewModel by viewModels<WordsViewModel>()

}