package io.github.sovathna.khmerdictionary.ui.words.history

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.ui.words.AbstractClearWordsFragment

@AndroidEntryPoint
class HistoryFragment : AbstractClearWordsFragment<HistoryViewModel>() {
  override val viewModel: HistoryViewModel by viewModels()
}