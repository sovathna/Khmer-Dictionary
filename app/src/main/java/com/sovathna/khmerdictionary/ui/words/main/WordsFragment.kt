package com.sovathna.khmerdictionary.ui.words.main

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.Logger
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.WordsState
import com.sovathna.khmerdictionary.ui.words.AbstractPagingWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable

@AndroidEntryPoint
class WordsFragment :
  AbstractPagingWordsFragment<WordsIntent, WordsState, WordsViewModel>() {

  override val viewModel: WordsViewModel by viewModels()

  override fun intents(): Observable<WordsIntent> =
    selectWordIntent.cast(WordsIntent::class.java)

  override fun onResume() {
    super.onResume()
    viewModel.wordsLiveData.observe(viewLifecycleOwner, Observer {
      pagingAdapter.submitData(lifecycle, it)
    })
  }
}