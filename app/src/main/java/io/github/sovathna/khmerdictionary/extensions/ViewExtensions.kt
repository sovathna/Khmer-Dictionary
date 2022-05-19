package io.github.sovathna.khmerdictionary.extensions

import android.app.Activity
import android.view.Gravity
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import io.github.sovathna.khmerdictionary.R

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


fun Activity.showSnack(
  parent: View,
  message: String,
  duration: Int = Snackbar.LENGTH_LONG,
  positive: String? = null,
  onAction: ((Int) -> Unit)? = null
): Snackbar {
  return Snackbar.make(parent, message, duration).apply {
    val snackLayout = view as Snackbar.SnackbarLayout
    snackLayout.setBackgroundResource(android.R.color.transparent)
    val custom = layoutInflater.inflate(R.layout.layout_snack, snackLayout, false)
    snackLayout.addView(custom)

    val tvMessage = custom.findViewById<MaterialTextView>(R.id.tv_snack_message)
    val btnPositive = custom.findViewById<MaterialButton>(R.id.btn_snack_positive)

    tvMessage.text = message

    val shouldShowPositive = !positive.isNullOrBlank()
    btnPositive.isVisible = shouldShowPositive
    if (shouldShowPositive) {
      tvMessage.gravity = Gravity.START
      btnPositive.text = positive
      btnPositive.setOnClickListener {
        onAction?.invoke(0)
        dismiss()
      }
    } else {
      tvMessage.gravity = Gravity.CENTER_HORIZONTAL
      btnPositive.text = null
      btnPositive.setOnClickListener(null)
    }
    show()
  }
}

fun Fragment.showSnack(
  parent: View,
  message: String,
  duration: Int = Snackbar.LENGTH_LONG,
  positive: String? = null,
  onAction: ((Int) -> Unit)? = null
): Snackbar {
  return requireActivity().showSnack(parent, message, duration, positive, onAction)
}