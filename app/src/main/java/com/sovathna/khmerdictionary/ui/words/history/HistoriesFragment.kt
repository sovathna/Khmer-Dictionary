package com.sovathna.khmerdictionary.ui.words.history

import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

@AndroidEntryPoint
class HistoriesFragment :
  AbstractWordsFragment<MviIntent, HistoriesState, HistoriesViewModel>() {

  override val viewModel: HistoriesViewModel by viewModels()

  private val getHistoriesIntent = PublishSubject.create<HistoriesIntent.GetWords>()

  @Inject
  lateinit var clearHistoriesIntent: PublishSubject<HistoriesIntent.ClearHistories>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(getHistoriesIntent, clearHistoriesIntent, selectWordIntent)

  override fun render(state: HistoriesState) {
    with(state) {
      wordsLiveData?.observe(viewLifecycleOwner) { submitData(it, true) }

      if (isInit) getHistoriesIntent.onNext(HistoriesIntent.GetWords(0, Const.PAGE_SIZE))

      loadSuccess?.getContentIfNotHandled()?.let {
        selectWordIntent.value?.word?.let {
          rv.post { selectWordIntent.onNext(WordsIntent.SelectWord(it)) }
        }
      }

    }
  }

}