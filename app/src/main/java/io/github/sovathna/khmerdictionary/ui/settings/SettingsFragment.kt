package io.github.sovathna.khmerdictionary.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.FragmentDialogChooseThemeBinding
import io.github.sovathna.khmerdictionary.databinding.FragmentSettingsBinding
import io.github.sovathna.khmerdictionary.extensions.setSafeClickListener
import io.github.sovathna.khmerdictionary.ui.viewBinding

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
  private val binding by viewBinding(FragmentSettingsBinding::bind)
  private val viewModel by viewModels<SettingsViewModel>()
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      layoutTheme.setOnClickListener {
        showChooseThemeDialog()
      }
    }

    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
  }

  private fun render(state: SettingsState) {
    with(binding) {
      with(state) {
        with(settings) {
          val nameRes = when (themeMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> R.string.theme_light
            AppCompatDelegate.MODE_NIGHT_YES -> R.string.theme_dark
            else -> R.string.theme_system
          }
          tvThemeSub.setText(nameRes)
        }

        changed?.getContentIfNotHandled()?.let {
          if (AppCompatDelegate.getDefaultNightMode() != settings.themeMode) {
            AppCompatDelegate.setDefaultNightMode(settings.themeMode)
          }
        }
      }
    }
  }

  private fun showChooseThemeDialog() {
    val binding = FragmentDialogChooseThemeBinding.inflate(layoutInflater, null, false)
    val builder = AlertDialog.Builder(requireContext())
    builder.setView(binding.root)
    val dialog = builder.show().apply {
      window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
    with(binding) {
      val shouldCheckId = when (viewModel.current.settings.themeMode) {
        AppCompatDelegate.MODE_NIGHT_NO -> btnThemeLight.id
        AppCompatDelegate.MODE_NIGHT_YES -> btnThemeDark.id
        else -> btnThemeSystem.id
      }
      rgTheme.check(shouldCheckId)
      rgTheme.setOnCheckedChangeListener { _, checkedId ->
        val pos = when (checkedId) {
          btnThemeLight.id -> 0
          btnThemeDark.id -> 1
          else -> 2
        }
        viewModel.selectTheme(pos)
        dialog.dismiss()
      }
      btnCancel.setSafeClickListener {
        dialog.dismiss()
      }
    }
  }
}