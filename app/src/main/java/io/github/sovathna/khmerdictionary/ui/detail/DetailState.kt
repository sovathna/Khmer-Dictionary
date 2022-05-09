package io.github.sovathna.khmerdictionary.ui.detail

import android.text.SpannableStringBuilder

data class DetailState(
  val isInit: Boolean = true,
  val word: String? = null,
  val definition: SpannableStringBuilder? = null
)