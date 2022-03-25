package io.github.sovathna.khmerdictionary.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import io.github.sovathna.khmerdictionary.databinding.ViewHolderWordBinding
import io.github.sovathna.khmerdictionary.model.WordEntity

class HomeAdapter : PagingDataAdapter<WordEntity, WordViewHolder>(ITEM_CALLBACK) {

    companion object {
        private val ITEM_CALLBACK = object : DiffUtil.ItemCallback<WordEntity>() {
            override fun areItemsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean =
                oldItem == newItem

        }
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderWordBinding.inflate(layoutInflater, parent, false)
        return WordViewHolder(binding)
    }
}