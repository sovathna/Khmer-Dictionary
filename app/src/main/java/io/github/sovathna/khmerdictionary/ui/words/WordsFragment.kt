package io.github.sovathna.khmerdictionary.ui.words

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.crazylegend.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentWordsBinding

@AndroidEntryPoint
class WordsFragment : Fragment(R.layout.fragment_words) {
    private val binding by viewBinding(FragmentWordsBinding::bind)
    private val viewModel by viewModels<WordsViewModel>()
    private lateinit var adapter: WordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = WordsAdapter(
            onLoadMore = {

            },
            onClick = {

            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: WordsState) {
        with(state) {
            with(binding) {
                recycler.adapter = adapter
            }

            adapter.submitList(words)
        }
    }
}