package io.github.sovathna.khmerdictionary.ui.home

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentHomeBinding
import io.github.sovathna.khmerdictionary.model.WordEntity
import io.github.sovathna.khmerdictionary.ui.main.MainViewModel
import io.github.sovathna.khmerdictionary.ui.viewBinding
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding by viewBinding(FragmentHomeBinding::bind)
  private val viewModel by viewModels<HomeViewModel>()
  private val mainViewModel by activityViewModels<MainViewModel>()
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
          viewModel.select(word, mainViewModel.current.detail)
        }
      }
    }
    type = (arguments?.getSerializable("type") as? HomeType) ?: HomeType.ALL
    if (savedInstanceState == null) {
      search("")
      mainViewModel.observeSelectedWord()
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.rv.adapter = adapter
    binding.rv.setRecycledViewPool(recycledViewPool)
    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
    onSearchChanged {
      search(it)
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
      launch {
        adapter.loadStateFlow.map {
          val append = it.append
          append is LoadState.NotLoading && append.endOfPaginationReached && adapter.itemCount == 0
        }.debounce(200L)
          .distinctUntilChanged()
          .collectLatest {
            binding.tvMessage.isVisible = it
          }
      }

      launch {
        binding.etSearch.textChangedFlow
          .debounce(600L)
          .distinctUntilChanged()
          .collectLatest {
            it?.let { onChanged(it) }
          }
      }
    }
  }

  private fun render(state: HomeState) {
    with(state) {
      observeData(paging)
      selectEvent?.getContentIfNotHandled()?.let {
        findNavController().navigate(R.id.action_to_detail)
      }
      scrollToTop(scrollToTopEvent)
    }
  }

  private fun observeData(paging: PagingData<WordEntity>?) {
    if (paging == null) return
    viewLifecycleOwner.lifecycleScope.launch {
      adapter.submitData(paging)
    }
  }

  private fun scrollToTop(event: Event<Unit>?) {
    event?.getContentIfNotHandled()?.let {
      binding.rv.scrollToTopWhenChanged()
    }
  }

}

fun RecyclerView.scrollToTopWhenChanged(isSmoothScroll: Boolean = false) {
  val onChanged = object : View.OnLayoutChangeListener {
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
      if (isSmoothScroll) {
        smoothScrollToPosition(0)
      } else {
        scrollToPosition(0)
      }
    }
  }
  addOnLayoutChangeListener(onChanged)
}

val EditText.textChangedFlow: Flow<String?>
  get() = callbackFlow {
    val watcher = addTextChangedListener {
      trySendBlocking(it?.toString())
    }
    awaitClose {
      removeTextChangedListener(watcher)
    }
  }

fun View.setOnSafeClick(delayMillis: Long = 600L, onClick: (View) -> Unit) {
  var last = 0L
  setOnClickListener {
    val current = System.currentTimeMillis()
    if (current - last >= delayMillis) {
      last = current
      onClick(it)
    }
  }
}