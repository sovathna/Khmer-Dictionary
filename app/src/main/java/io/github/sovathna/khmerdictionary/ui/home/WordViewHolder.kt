package io.github.sovathna.khmerdictionary.ui.home

import androidx.recyclerview.widget.RecyclerView
import io.github.sovathna.khmerdictionary.databinding.ViewHolderWordBinding
import io.github.sovathna.khmerdictionary.model.WordEntity

class WordViewHolder(private val binding: ViewHolderWordBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: WordEntity?) {
        if (item == null) return
        with(binding) {
            root.setOnClickListener { }
            tvWord.text = item.word
        }
    }
}