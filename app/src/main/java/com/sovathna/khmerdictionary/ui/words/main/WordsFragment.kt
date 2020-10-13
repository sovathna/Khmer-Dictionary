package com.sovathna.khmerdictionary.ui.words.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveDataReactiveStreams
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.WordsState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

@AndroidEntryPoint
class WordsFragment :
  AbstractWordsFragment<WordsIntent, WordsState, WordsViewModel>() {

  override val viewModel: WordsViewModel by viewModels()

  private val getWordsIntent = PublishSubject.create<WordsIntent.GetWords>()
  private val selectWord = BehaviorSubject.create<WordsIntent.SelectWord>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    LiveDataReactiveStreams.fromPublisher(
      selectWordIntent.toFlowable(BackpressureStrategy.BUFFER)
    ).observe(viewLifecycleOwner) { selectWord.onNext(it) }
  }

  override fun intents(): Observable<WordsIntent> =
    Observable.merge(getWordsIntent, selectWord)

  override fun render(state: WordsState) {
    with(state) {
      wordsLiveData?.observe(viewLifecycleOwner) { submitData(it) }
      if (isInit) getWordsIntent.onNext(WordsIntent.GetWords)
    }
  }
}