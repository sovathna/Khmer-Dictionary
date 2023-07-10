package io.github.sovathna.khmerdictionary.ui.splash

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentSplashBinding

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

  private val binding by viewBinding(FragmentSplashBinding::bind)
  private val viewModel by viewModels<SplashViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      btnRetry.setOnClickListener {
        viewModel.retry()
      }
    }
    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
  }

  private fun render(state: SplashState) {
    with(state) {
      with(binding) {
        progressBar.isVisible = isNotError
        tvSubTitle.isVisible = isNotError
        btnRetry.isVisible = isError

        val title = if (isError) {
          R.string.an_error_has_occurred
        } else if (isDeterminate) {
          R.string.loading
        } else if (type == SplashState.Type.DOWNLOADING) {
          R.string.downloading
        } else {
          R.string.loading
        }
        tvTitle.setText(title)

        if (isNotError) {
          progressBar.isIndeterminate = isDeterminate
          progressBar.max = (size * 100).toInt()
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress((read * 100).toInt(), true)
          } else {
            progressBar.progress = (read * 100).toInt()
          }
          tvSubTitle.isVisible = !isDeterminate
          tvSubTitle.text = getString(R.string.downloading_subtitle, read, size)
        }

        doneEvent?.getContentIfNotHandled()?.let {
          findNavController().navigate(R.id.to_main_fragment)
        }
      }
    }
  }

}