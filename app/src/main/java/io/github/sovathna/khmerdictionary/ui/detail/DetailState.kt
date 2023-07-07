package io.github.sovathna.khmerdictionary.ui.detail

import android.text.SpannableStringBuilder

data class DetailState(
  val id: Long = 0L,
  val word: String? = null,
  val definition: SpannableStringBuilder? = null,
  val fontSize: Float = 16.0f,
  val isBookmark: Boolean = false,
)
