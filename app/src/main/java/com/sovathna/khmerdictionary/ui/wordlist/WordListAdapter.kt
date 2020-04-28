package com.sovathna.khmerdictionary.ui.wordlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.R
import javax.inject.Inject

class WordListAdapter @Inject constructor() :
  ListAdapter<WordItem, RecyclerView.ViewHolder>(WordItem.ITEM_CALLBACK) {

  private var onItemClick: ((Int) -> Unit)? = null

  fun setOnItemClickListener(onItemClick: ((Int) -> Unit)?) {
    this.onItemClick = onItemClick
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_word, parent, false)
    return WordListViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is WordListViewHolder) {
      holder.bindView(getItem(position))
      holder.setItemClickListener(onItemClick)
    }
  }
}