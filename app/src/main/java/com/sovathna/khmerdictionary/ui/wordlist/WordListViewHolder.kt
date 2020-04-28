package com.sovathna.khmerdictionary.ui.wordlist

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.R

class WordListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val tvName = itemView.findViewById<AppCompatTextView>(R.id.tv_name)

  fun bindView(item: WordItem) {
    if(item.isSelected){
      tvName.text = item.word.name + " >"
    }else{
      tvName.text = item.word.name
    }
  }

  fun setItemClickListener(onItemClick: ((Int) -> Unit)?) {
    itemView.setOnClickListener(null)
    onItemClick?.let {
      itemView.setOnClickListener {
        it(adapterPosition)
      }
    }
  }

}