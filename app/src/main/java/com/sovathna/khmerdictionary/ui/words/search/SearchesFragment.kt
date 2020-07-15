package com.sovathna.khmerdictionary.ui.words.search

import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.Logger
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.words.AbstractPagingWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SearchesFragment :
  AbstractPagingWordsFragment<MviIntent, SearchWordsState, SearchesViewModel>() {

  override val viewModel: SearchesViewModel by viewModels()

  private val getWordsIntent = PublishSubject.create<SearchesIntent.GetWords>()

  @Inject
  lateinit var mainGetWordsIntent: PublishSubject<SearchesIntent.GetWords>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getWordsIntent,
      mainGetWordsIntent.debounce(400, TimeUnit.MILLISECONDS),
      selectWordIntent
    )

  override fun render(state: SearchWordsState) {
    super.render(state)
    with(state) {
      if (isInit) {
        getWordsIntent.onNext(SearchesIntent.GetWords(""))
      }
      loadSuccess?.getContentIfNotHandled()?.let {
        selectWordIntent.value?.word?.let {
          rv.postDelayed(200) {
            selectWordIntent.onNext(WordsIntent.SelectWord(it))
          }
        }
      }
    }
  }
}