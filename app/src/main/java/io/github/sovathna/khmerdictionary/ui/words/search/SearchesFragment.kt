package io.github.sovathna.khmerdictionary.ui.words.search

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import io.github.sovathna.khmerdictionary.ui.words.WordsType

@AndroidEntryPoint
class SearchesFragment : AbstractWordsFragment() {

    companion object {
        const val TAG = "searches_fragment"
    }

    override val viewModel by viewModels<SearchesViewModel>()

    fun search(searchTerm: String) {
        viewModel.getWords(WordsType.Searches(searchTerm))
    }

}