package io.github.sovathna.khmerdictionary.ui.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentAboutBinding
import io.github.sovathna.khmerdictionary.toKmStringNum
import io.github.sovathna.khmerdictionary.ui.viewBinding

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_about) {
  private val binding by viewBinding(FragmentAboutBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      tvVersion.text =
        getString(R.string.splash_version_text, BuildConfig.VERSION_NAME.toKmStringNum())
    }
  }
}