package io.github.sovathna.khmerdictionary.ui.words.bookmark

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.ui.words.AbstractClearWordsFragment

@AndroidEntryPoint
class BookmarkFragment : AbstractClearWordsFragment<BookmarkViewModel>() {
  override val viewModel: BookmarkViewModel by viewModels()
}