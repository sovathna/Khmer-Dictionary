package io.github.sovathna.khmerdictionary.ui.words.history

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsFragment

@AndroidEntryPoint
class HistoriesFragment : AbstractWordsFragment() {

  override val viewModel by viewModels<HistoriesViewModel>()

}