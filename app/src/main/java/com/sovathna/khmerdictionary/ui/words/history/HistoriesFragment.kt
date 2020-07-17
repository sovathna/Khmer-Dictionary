package com.sovathna.khmerdictionary.ui.words.history

import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.HistoriesState
import com.sovathna.khmerdictionary.ui.words.AbstractPagingWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

@AndroidEntryPoint
class HistoriesFragment :
  AbstractPagingWordsFragment<MviIntent, HistoriesState, HistoriesViewModel>() {

  override val viewModel: HistoriesViewModel by viewModels()

  @Inject
  lateinit var clearHistoriesIntent: PublishSubject<HistoriesIntent.ClearHistories>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      clearHistoriesIntent,
      selectWordIntent
    )

  override fun render(state: HistoriesState) {
    super.render(state)
    with(state) {
      loadSuccess?.getContentIfNotHandled()?.let {
        selectWordIntent.value?.word?.let {
          rv.post {
            selectWordIntent.onNext(WordsIntent.SelectWord(it))
          }
        }
      }
//      words?.let {
//        clearMenuItemLiveData.value = it.isNotEmpty()
//      }
    }
  }

}