package io.github.sovathna.khmerdictionary.ui.splash

import android.os.Build
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import io.github.sovathna.khmerdictionary.R

fun SplashFragment.render(state: SplashState) {
  with(binding) {
    val title = when {
      state.error != null -> R.string.splash_error
      state.isDownloading -> R.string.splash_downloading
      state.isDone -> R.string.splash_done
      else -> R.string.splash_preparing
    }
    tvTitle.setText(title)

    pb.isVisible = state.error == null
    pb.isIndeterminate = !state.isDownloading
    if (state.size > 0.0) {
      pb.max = (state.size * 1000).toInt()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        pb.setProgress((state.read * 1000).toInt(), true)
      } else {
        pb.progress = (state.read * 1000).toInt()
      }
    }

    tvSub.isVisible = state.isDownloading && state.error == null
    tvSub.text = String.format("%s/%s មេកាបៃ", state.readString, state.sizeString)

    btnRetry.isVisible = state.error != null

    state.redirectEvent?.getContentIfNotHandled()?.let {
      val action = SplashFragmentDirections.actionToHome()
      findNavController().navigate(action)
//      findNavController().graph.setStartDestination(R.id.home_fragment)
    }

  }
}