package io.github.sovathna.khmerdictionary.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.crazylegend.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentHomeBinding
import io.github.sovathna.khmerdictionary.ui.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

  private val binding by viewBinding(FragmentHomeBinding::bind)
  private val viewModel by viewModels<HomeViewModel>()
  private val mViewModel by activityViewModels<MainViewModel>()
  private lateinit var adapter: HomeAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter = HomeAdapter()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.rv.adapter = adapter
    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)

    viewLifecycleOwner.lifecycleScope.launch {
      launch {
        mViewModel.searchFlow.distinctUntilChanged().debounce(300L).collectLatest {
          viewModel.search(it)
          binding.rv.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
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
              binding.rv.removeOnLayoutChangeListener(this)
              binding.rv.scrollToPosition(0)
            }
          })
        }
      }
    }

  }

  private fun render(state: HomeState) {
    viewLifecycleOwner.lifecycleScope.launch {
      state.paging?.let {
        adapter.submitData(it)
      }
    }
  }

}