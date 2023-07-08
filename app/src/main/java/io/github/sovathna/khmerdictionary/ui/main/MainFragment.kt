package io.github.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.viewbinding.viewBinding
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.data.AppSettings
import io.github.sovathna.khmerdictionary.databinding.FragmentMainBinding
import io.github.sovathna.khmerdictionary.ui.detail.DetailFragment
import io.github.sovathna.khmerdictionary.ui.words.bookmark.BookmarksFragment
import io.github.sovathna.khmerdictionary.ui.words.history.HistoriesFragment
import io.github.sovathna.khmerdictionary.ui.words.home.WordsFragment
import io.github.sovathna.khmerdictionary.ui.words.search.SearchesFragment
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

  private val binding by viewBinding(FragmentMainBinding::bind)
  private lateinit var searchFragment: SearchesFragment
  private val searchFlow = MutableSharedFlow<String>()
  private lateinit var menuIds: List<Int>
  private var isSplit = false

  @Inject
  lateinit var settings: AppSettings

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    isSplit = resources.getBoolean(R.bool.is_split)
    menuIds =
      listOf(
        R.id.action_home,
        R.id.action_histories,
        R.id.action_bookmarks
      )
    searchFragment =
      if (savedInstanceState == null) {
        SearchesFragment()
      } else {
        childFragmentManager.findFragmentByTag(SearchesFragment.TAG) as SearchesFragment
      }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (savedInstanceState == null) {
      childFragmentManager.beginTransaction()
        .replace(binding.searchFragmentContainer.id, searchFragment, SearchesFragment.TAG)
        .commit()
    }

    viewLifecycleOwner.lifecycleScope.launch {
      searchFlow
        .distinctUntilChanged()
        .debounce(500L)
        .collectLatest {
          searchFragment.search(it.trim())
        }
    }
    val fragments = listOf(
      WordsFragment(),
      HistoriesFragment(),
      BookmarksFragment()
    )
    val adapter = MainPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)

    with(binding) {

      viewLifecycleOwner.lifecycleScope.launch {
        settings.detailIdFlow.distinctUntilChanged().collectLatest {
          if (isSplit && searchView.isShowing) {
            searchView.hide()
          }
        }
      }

      navigationView.isVisible = !isSplit
      navigationRail.isVisible = isSplit

      searchView
        .editText
        .addTextChangedListener {
          viewLifecycleOwner.lifecycleScope.launch {
            searchFlow.emit(it?.toString() ?: "")
          }
        }

      searchView.addTransitionListener { _, _, newState ->
        if (newState == SearchView.TransitionState.HIDDEN) {
          viewLifecycleOwner.lifecycleScope.launch {
            searchFlow.emit("")
          }
        }
      }

      searchBar.inflateMenu(R.menu.menu_main)
      searchBar.setOnMenuItemClickListener {
        if (it.itemId == R.id.action_settings) {
          findNavController().navigate(R.id.to_settings_fragment)
          return@setOnMenuItemClickListener true
        }
        false
      }
      viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
          if (!isSplit) {
            navigationView.selectedItemId = menuIds[position]
          } else {
            navigationRail.selectedItemId = menuIds[position]
          }
        }
      })
      if (!isSplit) {
        navigationView.setOnItemSelectedListener {
          viewPager.currentItem = menuIds.indexOf(it.itemId)
          true
        }
      } else {
        navigationRail.setOnItemSelectedListener {
          viewPager.setCurrentItem(menuIds.indexOf(it.itemId), false)
          true
        }
      }
      viewPager.isUserInputEnabled = !isSplit
      viewPager.adapter = adapter

      if (savedInstanceState == null) {
        childFragmentManager
          .beginTransaction()
          .replace(detailContainer.id, DetailFragment(), DetailFragment.TAG)
          .commit()
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.viewPager.adapter = null
  }

  private var callback: OnBackPressedCallback? = null
  override fun onResume() {
    super.onResume()
    viewLifecycleOwner.lifecycleScope.launch {
      if (binding.searchView.isShowing) {
        binding.searchView.requestFocusAndShowKeyboard()
      }
    }
    callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
      with(binding) {
        if (searchView.isShowing && searchView.editText.text.isNotBlank()) {
          searchView.editText.text = null
        } else if (searchView.isShowing && searchView.editText.text.isBlank()) {
          searchView.hide()
        } else if (viewPager.currentItem != 0) {
          viewPager.currentItem = 0
        } else {
          remove()
          requireActivity().onBackPressedDispatcher.onBackPressed()
        }
      }
    }
  }

  override fun onPause() {
    super.onPause()
    callback?.remove()
  }

}