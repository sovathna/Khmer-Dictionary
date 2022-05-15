package io.github.sovathna.khmerdictionary.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentHomeBinding
import io.github.sovathna.khmerdictionary.extensions.scrollToTopOnLayoutChanged
import io.github.sovathna.khmerdictionary.model.WordEntity
import io.github.sovathna.khmerdictionary.ui.main.MainViewModel
import io.github.sovathna.khmerdictionary.ui.viewBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
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
    setHasOptionsMenu(true)
    adapter = HomeAdapter { word, id ->
      when (id) {
        R.id.btn_bookmark -> mainViewModel.updateBookmark(word.id, !word.isBookmark)
        R.id.root -> {
          mainViewModel.select(word.id)
          if (resources.getBoolean(R.bool.is_single))
            findNavController().navigate(R.id.action_to_detail)
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
    viewLifecycleOwner.lifecycleScope.launch {
      adapter.loadStateFlow.map {
        val append = it.append
        append is LoadState.NotLoading && append.endOfPaginationReached && adapter.itemCount == 0
      }.debounce(200L)
        .distinctUntilChanged()
        .collectLatest {
          binding.tvMessage.isVisible = it
        }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.rv.setRecycledViewPool(null)
  }

  private fun search(searchTerm: String) {
    viewModel.search(searchTerm, type)
  }

  private fun render(state: HomeState) {
    with(state) {
      observeData(paging)
      scrollToTop(scrollToTopEvent)
      test()
    }
  }

  private fun test() {
    val searchTerm = viewModel.current.searchTerm
    val searchMenu = this.searchMenu
    if (searchMenu != null) {
      if (!searchMenu.isActionViewExpanded && !searchTerm.isNullOrBlank()) {
        searchMenu.expandActionView()
        searchView?.setQuery(searchTerm, false)
      }
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
      binding.rv.scrollToTopOnLayoutChanged()
    }
  }

  private var searchMenu: MenuItem? = null
  private val searchView: SearchView? get() = searchMenu?.actionView as? SearchView

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_home, menu)
    searchMenu = menu.findItem(R.id.menu_search)
    searchView?.let { searchView ->
      searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)?.apply {
        typeface = ResourcesCompat.getFont(requireContext(), R.font.suwannaphum)
      }
      searchView.queryHint = getString(R.string.search_word)
      viewLifecycleOwner.lifecycleScope.launch {
        searchView.queryTextChangedFlow
          .debounce(600L)
          .distinctUntilChanged()
          .collectLatest { search(it) }
      }
    }
    test()
  }

}

val SearchView.queryTextChangedFlow: Flow<String>
  get() = callbackFlow {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
          trySendBlocking(newText)
        }
        return false
      }
    })
    awaitClose {
      setOnQueryTextListener(null)
    }
  }