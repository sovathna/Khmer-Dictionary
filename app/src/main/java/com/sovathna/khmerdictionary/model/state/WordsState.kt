package com.sovathna.khmerdictionary.model.state

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.ui.words.WordItem

data class WordsState(
  override val isInit: Boolean = true,
  override val loadSuccess: Event<Unit>? = null,
  override val wordsLiveData: LiveData<PagingData<WordItem>>? = null
) : AbstractPagingWordsState()