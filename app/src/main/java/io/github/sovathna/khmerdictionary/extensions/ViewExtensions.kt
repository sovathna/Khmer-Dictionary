package io.github.sovathna.khmerdictionary.extensions

import android.app.Activity
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

private var snackbar: Snackbar? = null

fun Activity.showSnack(view: View, message: String, positive: String?, onAction: (Int) -> Unit) {
  snackbar?.dismiss()
  snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
  snackbar?.let { snackbar ->
    val snackLayout = snackbar.view as Snackbar.SnackbarLayout
    snackLayout.setBackgroundResource(android.R.color.transparent)
    val custom = layoutInflater.inflate(R.layout.layout_snack, snackLayout, false)
    val tvMessage = custom.findViewById<MaterialTextView>(R.id.tv_snack_message)
    tvMessage.text = message
    val btnPositive = custom.findViewById<MaterialButton>(R.id.btn_snack_positive)
    btnPositive.text = positive
    btnPositive.isVisible = !positive.isNullOrBlank()
    btnPositive.setOnClickListener {
      onAction(0)
      snackbar.dismiss()
    }
    snackLayout.addView(custom)
  }
  snackbar?.show()
}

fun Fragment.showSnack(view: View, message: String, positive: String?, onAction: (Int) -> Unit) {
  requireActivity().showSnack(view, message, positive, onAction)
}