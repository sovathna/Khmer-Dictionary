package com.sovathna.khmerdictionary.model.state

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.ui.words.WordItem

abstract class AbstractPagingWordsState : MviState {
  abstract val isInit: Boolean
  abstract val loadSuccess: Event<Unit>?
  abstract val wordsLiveData: LiveData<PagingData<WordItem>>?
}