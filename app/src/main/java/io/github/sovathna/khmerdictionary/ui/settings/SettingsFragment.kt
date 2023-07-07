package io.github.sovathna.khmerdictionary.ui.settings

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.viewbinding.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.DialogFontSizeBinding
import io.github.sovathna.khmerdictionary.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

  private val binding by viewBinding(FragmentSettingsBinding::bind)
  private val viewModel by viewModels<SettingsViewModel>()
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      toolbar.setNavigationOnClickListener {
        findNavController().popBackStack()
      }

      cardFontSize.setOnClickListener {
        showFontSizeDialog()
      }
    }

    viewModel.stateLiveData.observe(viewLifecycleOwner, ::render)
  }

  private fun render(state: SettingsState) {
    with(state) {
      with(binding) {
        tvFontSizeValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        tvFontSizeValue.text = toKmString(fontSize)
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
      .setPositiveButton("យល់ព្រម") { _, _ ->
        viewModel.setFontSize(tmpBinding.slider.value)
      }
      .setNegativeButton("បោះបង់", null)
      .setView(tmpBinding.root)
      .show()
  }
}