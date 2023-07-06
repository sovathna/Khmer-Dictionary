package io.github.sovathna.khmerdictionary.ui.detail

import android.text.SpannableStringBuilder

data class DetailState(
  val id: Long = 0L,
  val word: String? = null,
  val definition: SpannableStringBuilder? = null,
  val isBookmark: Boolean = false
)
