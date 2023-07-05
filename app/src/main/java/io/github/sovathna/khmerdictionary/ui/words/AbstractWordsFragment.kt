package io.github.sovathna.khmerdictionary.ui.words

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.viewbinding.viewBinding
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentWordsBinding
import io.github.sovathna.khmerdictionary.model.ui.WordUi


abstract class AbstractWordsFragment : Fragment(R.layout.fragment_words) {
    private val binding by viewBinding(FragmentWordsBinding::bind)
    protected abstract val viewModel: AbstractWordsViewModel
    private lateinit var adapter: WordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = WordsAdapter(onLoadMore = viewModel::getMore, onClick = ::onClick)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.adapter = adapter
        }
        viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
    }

    private fun onClick(word: WordUi) {

    }

    private fun render(state: WordsState) {
        with(state) {
            with(binding) {
                tvEmpty.isVisible = isEmpty || isSearchAndNull
                when {
                    isEmpty -> {
                        tvEmpty.setText(R.string.empty)
                    }

                    isSearchAndNull -> tvEmpty.setText(R.string.search_message)
                }
                recycler.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        recycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        if (page == 1) recycler.smoothScrollToPosition(0)
                    }
                })
            }

            adapter.submitList(words)
        }
    }
}