package io.github.sovathna.khmerdictionary.ui.words

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.extensions.showSnack

abstract class AbstractClearWordsFragment<VM : AbstractClearWordsViewModel> :
  AbstractWordsFragment<VM>() {

  private var clearItem: MenuItem? = null

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.menu_clear, menu)
    clearItem = menu.findItem(R.id.action_clear)
    clearItemVisibility()
  }

  override fun render(state: WordsState) {
    super.render(state)
    clearItemVisibility()
  }

  private fun clearItemVisibility() {
    clearItem?.isVisible = viewModel.current.isEmpty == false
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.action_clear) {
      showSnack(binding.root, "លុបបញ្ជី?", "លុប") {
        viewModel.clearWords()
      }
      return true
    }
    return super.onOptionsItemSelected(item)
  }
}