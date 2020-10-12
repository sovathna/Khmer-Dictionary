package com.sovathna.khmerdictionary.ui.words

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.R

class WordItemViewHolder(itemView: View, onItemClick: ((Int, WordItem) -> Unit)?) :
  RecyclerView.ViewHolder(itemView) {

  private val tvName = itemView.findViewById<AppCompatTextView>(R.id.tv_name)
  private val divider = itemView.findViewById<View>(R.id.divider_end)

  private var item: WordItem? = null

  init {
    itemView.setOnClickListener {
      val item = this.item
      if (onItemClick != null && item != null) onItemClick(bindingAdapterPosition, item)
    }
  }

  fun bindView(item: WordItem) {
    this.item = item
    if (item.isSelected) {
      tvName.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
      tvName.setBackgroundColor(
        ContextCompat.getColor(
          itemView.context,
          R.color.color_item_bg_selected
        )
      )
      divider.visibility = View.GONE
    } else {
      tvName.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.black))
      tvName.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.color_item_bg))
      divider.visibility = View.VISIBLE
    }
    tvName.text = item.word.name
  }

}