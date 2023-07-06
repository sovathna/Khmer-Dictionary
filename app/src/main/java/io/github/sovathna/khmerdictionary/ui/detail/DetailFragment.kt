package io.github.sovathna.khmerdictionary.ui.detail

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.crazylegend.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentDetailBinding

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
  private val binding by viewBinding(FragmentDetailBinding::bind)
  private val viewModel by viewModels<DetailViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      tvDefinition.movementMethod = LinkMovementMethod.getInstance()
    }
    viewModel.stateLiveData.observe(viewLifecycleOwner,::render)
  }

  private fun render(state:DetailState){
    with(state){
      with(binding){
        tvWord.text = word
        tvDefinition.text = definition
      }
    }
  }
}