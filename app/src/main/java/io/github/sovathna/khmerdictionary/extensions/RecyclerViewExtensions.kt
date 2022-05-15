package io.github.sovathna.khmerdictionary.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToTopOnLayoutChanged(isSmoothScroll: Boolean = false) =
  scrollToPositionOnLayoutChanged(0, isSmoothScroll)


fun RecyclerView.scrollToPositionOnLayoutChanged(position: Int, isSmoothScroll: Boolean = false) {
  val onChanged = object : View.OnLayoutChangeListener {
    override fun onLayoutChange(
      v: View?,
      left: Int,
      top: Int,
      right: Int,
      bottom: Int,
      oldLeft: Int,
      oldTop: Int,
      oldRight: Int,
      oldBottom: Int
    ) {
      removeOnLayoutChangeListener(this)
      if (isSmoothScroll) {
        smoothScrollToPosition(position)
      } else {
        scrollToPosition(position)
      }
    }
  }
  addOnLayoutChangeListener(onChanged)
}