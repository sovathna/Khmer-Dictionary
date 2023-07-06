package io.github.sovathna.khmerdictionary.ui.words.bookmark

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsFragment

@AndroidEntryPoint
class BookmarksFragment : AbstractWordsFragment() {

  override val viewModel by viewModels<BookmarksViewModel>()
  override fun onResume() {
    super.onResume()
    viewModel.getWords()
  }
}