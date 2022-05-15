package io.github.sovathna.khmerdictionary.extensions

import android.view.View

fun View.setSafeClickListener(delayMillis: Long = 600L, onClick: (View) -> Unit) {
  var last = 0L
  setOnClickListener {
    val current = System.currentTimeMillis()
    if (current - last >= delayMillis) {
      last = current
      onClick(it)
    }
  }
}