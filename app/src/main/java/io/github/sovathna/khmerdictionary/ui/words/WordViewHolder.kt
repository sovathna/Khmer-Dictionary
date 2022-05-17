package io.github.sovathna.khmerdictionary.ui.words

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.ViewHolderWordBinding
import io.github.sovathna.khmerdictionary.extensions.setSafeClickListener
import io.github.sovathna.khmerdictionary.model.WordEntity

class WordViewHolder(private val binding: ViewHolderWordBinding) :
  RecyclerView.ViewHolder(binding.root) {

  fun bindView(item: WordEntity?, clickListener: (WordEntity, Int) -> Unit) {
    if (item == null) return
    with(binding) {
      root.strokeColor = ContextCompat.getColor(root.context,R.color.md_theme_light_primary)
      root.setSafeClickListener { clickListener(item, it.id) }
      btnBookmark.setSafeClickListener { clickListener(item, it.id) }
      tvWord.text = item.word
//      root.setCardBackgroundColor(Co)
      val colorRes = if (item.isSelect) R.color.color_selected else R.color.color_normal
      tvWord.setTextColor(ContextCompat.getColor(root.context, colorRes))
      val bookmarkRes =
        if (item.isBookmark) R.drawable.round_bookmark_24 else R.drawable.round_bookmark_border_24
      btnBookmark.setIconResource(bookmarkRes)
    }
  }
}