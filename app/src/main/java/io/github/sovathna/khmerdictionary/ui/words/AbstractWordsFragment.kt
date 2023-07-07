package io.github.sovathna.khmerdictionary.ui.words

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.viewbinding.viewBinding
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentWordsBinding
import io.github.sovathna.khmerdictionary.model.ui.WordUi
import javax.inject.Inject


abstract class AbstractWordsFragment : Fragment(R.layout.fragment_words) {
  private val binding by viewBinding(FragmentWordsBinding::bind)
  protected abstract val viewModel: AbstractWordsViewModel
  private lateinit var adapter: WordsAdapter

  @Inject
  protected lateinit var recycledViewPool: RecyclerView.RecycledViewPool

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter = WordsAdapter(onLoadMore = viewModel::getMore, onClick = ::onClick)
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      recycler.setRecycledViewPool(recycledViewPool)
      recycler.layoutManager = LinearLayoutManager(requireContext())
      recycler.adapter = adapter
    }
    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.recycler.adapter = null
    binding.recycler.setRecycledViewPool(null)
  }

  private fun onClick(word: WordUi) {
    viewModel.select(word.id)
  }

  private fun render(state: WordsState) {
    with(state) {
      with(binding) {
        tvEmpty.isVisible = isEmpty && !isSearch
        tvSearchMessage.isVisible = isSearchAndNull
        tvSearchEmpty.isVisible = isSearch && isEmpty
//        if (type is WordsType.Searches) {
//          recycler.viewTreeObserver.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//              recycler.viewTreeObserver.removeOnGlobalLayoutListener(this)
//              if (page == 1) recycler.smoothScrollToPosition(0)
//            }
//          })
//        }

        detailEvent?.getContentIfNotHandled()?.let {
          findNavController().navigate(R.id.to_detail_fragment)
        }
      }

      adapter.submitList(words)
    }
  }
}