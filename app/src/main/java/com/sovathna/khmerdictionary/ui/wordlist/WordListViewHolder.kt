package com.sovathna.khmerdictionary.ui.wordlist

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.R

class WordListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  private val tvName = itemView.findViewById<AppCompatTextView>(R.id.tv_name)
  private val divider = itemView.findViewById<View>(R.id.divider_1)
  fun bindView(item: WordItem) {
    if (item.isSelected) {
      tvName.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
      divider.visibility = View.GONE
    } else {
      tvName.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
      divider.visibility = View.VISIBLE
    }
    tvName.text = item.word.name
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