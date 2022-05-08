package io.github.sovathna.khmerdictionary.ui.home

import androidx.recyclerview.widget.RecyclerView
import io.github.sovathna.khmerdictionary.R
import io.github.sovathna.khmerdictionary.databinding.ViewHolderWordBinding
import io.github.sovathna.khmerdictionary.model.WordEntity

class WordViewHolder(private val binding: ViewHolderWordBinding) :
  RecyclerView.ViewHolder(binding.root) {

  fun bindView(item: WordEntity?, clickListener: (WordEntity, Int) -> Unit) {
    if (item == null) return
    with(binding) {
      root.setOnClickListener { clickListener(item, it.id) }
      btnBookmark.setOnClickListener { clickListener(item, it.id) }
      tvWord.text = item.word

      val bookmarkRes =
        if (item.favorite) R.drawable.round_bookmark_24
        else R.drawable.round_bookmark_border_24
      btnBookmark.setIconResource(bookmarkRes)
    }
  }
}