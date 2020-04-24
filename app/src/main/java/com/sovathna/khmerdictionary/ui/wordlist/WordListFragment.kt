package com.sovathna.khmerdictionary.ui.wordlist

import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WordListFragment : MviFragment<WordListIntent, WordListState, WordListViewModel>(
  R.layout.fragment_word_list
) {

  @Inject
  lateinit var getWordListIntent: PublishSubject<WordListIntent.Get>

  override fun intents(): Observable<WordListIntent> =
    getWordListIntent
      .throttleLast(500, TimeUnit.MILLISECONDS)
      .cast(WordListIntent::class.java)

  override fun render(state: WordListState) {
    LogUtil.i("state: $state")
    with(state) {
      if (isInit) getWordListIntent.onNext(WordListIntent.Get(null, 0))
    }
  }
}