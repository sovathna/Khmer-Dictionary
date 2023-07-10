package io.github.sovathna.khmerdictionary.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.crazylegend.viewbinding.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.BuildConfig
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.DialogFontSizeBinding
import io.github.sovathna.khmerdictionary.databinding.FragmentSettingsBinding
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

  private val binding by viewBinding(FragmentSettingsBinding::bind)
  private val viewModel by viewModels<SettingsViewModel>()
  private lateinit var themes: Array<String>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    themes = resources.getStringArray(R.array.themes)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      toolbar.setNavigationOnClickListener {
        findNavController().popBackStack()
      }

      cardTheme.setOnClickListener {
        showThemeDialog()
      }

      cardFontSize.setOnClickListener {
        showFontSizeDialog()
      }

      btnPrivacyPolicy.setOnClickListener {
        try {
          val privacyUrl = "https://sovathna.github.io/khdict/privacy/"
          val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
          startActivity(intent)
        } catch (e: Exception) {
          Timber.tag("debug").e(e)
        }
      }

      btnRateReview.setOnClickListener {
        try {
          val intent =
            Intent(
              Intent.ACTION_VIEW,
              Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
            )
          startActivity(intent)
        } catch (e: Exception) {
          Timber.tag("debug").e(e)
          try {
            val marketUrl = "https://play.google.com/store/apps/details?id="
            val intent =
              Intent(Intent.ACTION_VIEW, Uri.parse("$marketUrl${BuildConfig.APPLICATION_ID}"))
            startActivity(intent)
          } catch (e: Exception) {
            Timber.tag("debug").e(e)
          }
        }
      }
    }

    viewModel.stateLiveData.distinctUntilChanged().observe(viewLifecycleOwner, ::render)
  }

  private fun render(state: SettingsState) {
    Timber.tag("debug").d("$state")
    with(state) {
      with(binding) {
        tvFontSizeValue.text = toKmString(fontSize)

        tvThemeValue.text = themes[nightMode]
      }
    }
  }

  private fun toKmString(fontSize: Float): String {
    val tmp = fontSize.toInt().toString()
    return tmp.map { tmp("$it") }.reduce { acc, s -> acc + s }
  }

  private fun tmp(text: String): String {
    return when (text) {
      "0" -> "០"
      "1" -> "១"
      "2" -> "២"
      "3" -> "៣"
      "4" -> "៤"
      "5" -> "៥"
      "6" -> "៦"
      "7" -> "៧"
      "8" -> "៨"
      "9" -> "៩"
      else -> text
    }
  }

  private fun showFontSizeDialog() {
    val tmpBinding = DialogFontSizeBinding.inflate(layoutInflater, null, false)
    with(tmpBinding) {
      val fontSize = viewModel.stateLiveData.value!!.fontSize
      tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
      slider.value = fontSize
      slider.addOnChangeListener { _, value, _ ->
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
      }
    }
    MaterialAlertDialogBuilder(requireContext())
      .setTitle(R.string.settings_font_size)
      .setPositiveButton(R.string.confirm) { _, _ ->
        viewModel.setFontSize(tmpBinding.slider.value)
      }
      .setNegativeButton(R.string.cancel, null)
      .setView(tmpBinding.root)
      .show()
  }

  private fun showThemeDialog() {
    MaterialAlertDialogBuilder(requireContext())
      .setTitle(R.string.settings_night_mode)
      .setSingleChoiceItems(themes, viewModel.stateLiveData.value!!.nightMode) { dialog, which ->
        dialog.dismiss()
        viewModel.setNightMode(if (which == 0) -1 else which)
      }
      .setNegativeButton(R.string.exit, null)
      .show()
  }
}