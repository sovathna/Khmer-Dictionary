package io.github.sovathna.khmerdictionary.ui.detail

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentDetailBinding
import io.github.sovathna.khmerdictionary.ui.viewBinding

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
  private val binding by viewBinding(FragmentDetailBinding::bind)
  private val viewModel by viewModels<DetailViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      tvDefinition.movementMethod = LinkMovementMethod.getInstance()
    }
    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
  }

  private fun render(state: DetailState) {
    if (state.isInit) getDetail()
    binding.tvWord.text = state.word
    binding.tvDefinition.text = state.definition
  }

  private fun getDetail() {
    arguments?.getLong("word_id")?.let {
      viewModel.getDetail(it)
    }
  }

}