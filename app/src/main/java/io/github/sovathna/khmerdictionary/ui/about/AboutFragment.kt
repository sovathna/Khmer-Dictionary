package io.github.sovathna.khmerdictionary.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentAboutBinding
import io.github.sovathna.khmerdictionary.extensions.kmNumString
import io.github.sovathna.khmerdictionary.ui.viewBinding

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_about) {
  private val binding by viewBinding(FragmentAboutBinding::bind)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      tvVersion.text =
        getString(R.string.splash_version_text, BuildConfig.VERSION_NAME.kmNumString())
      with(layoutAboutButtons) {
        btnGithub.setOnClickListener {
          val url = "https://github.com/sovathna/Khmer-Dictionary"
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
          startActivity(intent)
        }
        btnCreator.setOnClickListener {
          val url = "https://sovathna.github.io/"
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
          startActivity(intent)
        }
      }
    }
  }
}