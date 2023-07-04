package io.github.sovathna.khmerdictionary.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.sovathna.khmerdictionary.databinding.ViewHolderWordBinding
import io.github.sovathna.khmerdictionary.model.ui.WordUi

class WordsAdapter(
    private val onLoadMore: () -> Unit,
    private val onClick: (WordUi) -> Unit
) : ListAdapter<WordUi, WordViewHolder>(ITEM_CALLBACK) {

    companion object {
        private val ITEM_CALLBACK = object : DiffUtil.ItemCallback<WordUi>() {

            override fun areItemsTheSame(oldItem: WordUi, newItem: WordUi): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: WordUi, newItem: WordUi): Boolean =
                oldItem == newItem
        }
    }

    private var isLoadMore = false

    override fun onCurrentListChanged(
        previousList: MutableList<WordUi>,
        currentList: MutableList<WordUi>
    ) {
        super.onCurrentListChanged(previousList, currentList)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderWordBinding.inflate(inflater)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
        if(position >= (itemCount-20)){
            isLoadMore = true
        }
    }
}