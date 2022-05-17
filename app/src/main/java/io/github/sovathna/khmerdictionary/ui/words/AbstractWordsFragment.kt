package io.github.sovathna.khmerdictionary.ui.words

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import io.github.sovathna.khmerdictionary.Event
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentWordsBinding
import io.github.sovathna.khmerdictionary.extensions.queryTextChangedFlow
import io.github.sovathna.khmerdictionary.extensions.scrollToTopOnLayoutChanged
import io.github.sovathna.khmerdictionary.model.WordEntity
import io.github.sovathna.khmerdictionary.ui.main.MainViewModel
import io.github.sovathna.khmerdictionary.ui.viewBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
abstract class AbstractWordsFragment<VM : AbstractWordsViewModel> :
  Fragment(R.layout.fragment_words) {

  protected val binding by viewBinding(FragmentWordsBinding::bind)
  private val mainViewModel by activityViewModels<MainViewModel>()
  private lateinit var adapter: WordsAdapter
  private var searchMenu: MenuItem? = null
  private val searchView: SearchView? get() = searchMenu?.actionView as? SearchView

  protected abstract val viewModel: VM

  @Inject
  protected lateinit var recycledViewPool: RecyclerView.RecycledViewPool

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    adapter = WordsAdapter { word, id ->
      when (id) {
        R.id.btn_bookmark -> mainViewModel.updateBookmark(word.id, !word.isBookmark)
        R.id.root -> {
          mainViewModel.select(word.id)
          if (resources.getBoolean(R.bool.is_single))
            findNavController().navigate(R.id.action_to_detail)
        }
      }
    }
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
          viewModel.setEmptyState(it)
        }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.rv.setRecycledViewPool(null)
  }

  private fun search(searchTerm: String) {
    viewModel.search(searchTerm)
  }

  protected open fun render(state: WordsState) {
    with(state) {
      binding.tvMessage.isVisible = isEmpty == true
      observeData(paging)
      scrollToTop(scrollToTopEvent)
      restoreSearchView()
    }
  }

  private fun restoreSearchView() {
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

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_home, menu)
    searchMenu = menu.findItem(R.id.action_search)
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
    restoreSearchView()
  }
}