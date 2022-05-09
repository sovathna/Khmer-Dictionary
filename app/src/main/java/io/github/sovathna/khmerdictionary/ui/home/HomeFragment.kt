package io.github.sovathna.khmerdictionary.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentHomeBinding
import io.github.sovathna.khmerdictionary.ui.main.MainViewModel
import io.github.sovathna.khmerdictionary.ui.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding by viewBinding(FragmentHomeBinding::bind)
  private val viewModel by viewModels<HomeViewModel>()
  private val mViewModel by activityViewModels<MainViewModel>()
  private lateinit var adapter: HomeAdapter

  private lateinit var type: HomeType

  @Inject
  lateinit var recycledViewPool: RecyclerView.RecycledViewPool

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter = HomeAdapter { word, id ->
      when (id) {
        R.id.btn_bookmark -> viewModel.updateBookmark(word)
        R.id.root -> {
          findNavController().navigate(
            R.id.nav_detail,
            Bundle().apply {
              putLong("word_id", word.id)
            }
          )
          viewModel.updateHistory(word)
        }
      }
    }
    type = (arguments?.getSerializable("type") as? HomeType) ?: HomeType.ALL
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.rv.adapter = adapter
    binding.rv.setRecycledViewPool(recycledViewPool)
    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
    onSearchChanged {
      search(it)
      scrollToTop()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.rv.setRecycledViewPool(null)
  }

  private fun search(searchTerm: String) {
    viewModel.search(searchTerm, type)
  }

  private fun onSearchChanged(onChanged: (String) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
      mViewModel.searchFlow
        .distinctUntilChanged()
        .debounce(400L)
        .collectLatest {
          onChanged(it)
        }
    }
  }

  private fun render(state: HomeState) {
    viewLifecycleOwner.lifecycleScope.launch {
      launch {
        state.paging?.let {
          adapter.submitData(it)
        }
      }
      launch {
        adapter.loadStateFlow.map {
          val append = it.append
          append is LoadState.NotLoading && append.endOfPaginationReached && adapter.itemCount == 0
        }.distinctUntilChanged().debounce(100).collectLatest {
          binding.tvMessage.isVisible = it
        }
      }
    }
  }

  private fun scrollToTop() {
    with(binding.rv) {
      addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
          v: View?,
          left: Int,
          top: Int,
          right: Int,
          bottom: Int,
          oldLeft: Int,
          oldTop: Int,
          oldRight: Int,
          oldBottom: Int
        ) {
          removeOnLayoutChangeListener(this)
          scrollToPosition(0)
        }
      })
    }
  }

}