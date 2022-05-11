package io.github.sovathna.khmerdictionary.ui.detail

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentDetailBinding
import io.github.sovathna.khmerdictionary.ui.main.MainViewModel
import io.github.sovathna.khmerdictionary.ui.viewBinding

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
  private val binding by viewBinding(FragmentDetailBinding::bind)
  private val mainViewModel by activityViewModels<MainViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      tvDefinition.movementMethod = LinkMovementMethod.getInstance()
    }
    mainViewModel.stateLiveData.observe(viewLifecycleOwner) {
      render(it.detail)
    }
  }

//  override fun onDestroyView() {
//    super.onDestroyView()
//    mainViewModel.stateLiveData.removeObservers(viewLifecycleOwner)
//  }

  private fun render(state: DetailState?) {
    binding.tvWord.text = state?.word
    binding.tvDefinition.text = state?.definition
  }
}