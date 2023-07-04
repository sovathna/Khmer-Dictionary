package io.github.sovathna.khmerdictionary.ui.words

import androidx.recyclerview.widget.RecyclerView
import io.github.sovathna.khmerdictionary.databinding.ViewHolderWordBinding
import io.github.sovathna.khmerdictionary.model.ui.WordUi

class WordViewHolder(
    private val binding: ViewHolderWordBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(word: WordUi, onClick: (WordUi) -> Unit) {
        with(binding) {
            root.setOnClickListener { onClick(word) }
            tvWord.text = word.word
        }
    }
}