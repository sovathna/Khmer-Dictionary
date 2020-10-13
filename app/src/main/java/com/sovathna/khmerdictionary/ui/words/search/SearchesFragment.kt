package com.sovathna.khmerdictionary.ui.words.search

import android.os.Bundle
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveDataReactiveStreams
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SearchesFragment :
  AbstractWordsFragment<MviIntent, SearchWordsState, SearchesViewModel>() {

  override val viewModel: SearchesViewModel by viewModels()

  private val getWordsIntent = PublishSubject.create<SearchesIntent.GetWords>()

  @Inject
  lateinit var mainGetWordsIntent: PublishSubject<SearchesIntent.GetWords>

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    LiveDataReactiveStreams
      .fromPublisher(mainGetWordsIntent.toFlowable(BackpressureStrategy.BUFFER))
      .observe(viewLifecycleOwner) {
        viewModel.stateLiveData.value?.wordsLiveData?.removeObservers(viewLifecycleOwner)
      }
  }

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getWordsIntent,
      mainGetWordsIntent.debounce(400, TimeUnit.MILLISECONDS),
      selectWordIntent
    )

  override fun render(state: SearchWordsState) {
    with(state) {

      wordsLiveData?.observe(viewLifecycleOwner) { submitData(it) }

      if (isInit) getWordsIntent.onNext(SearchesIntent.GetWords(""))

      loadSuccess?.getContentIfNotHandled()?.let {
        selectWordIntent.value?.word?.let {
          rv.postDelayed(200) { selectWordIntent.onNext(WordsIntent.SelectWord(it)) }
        }
      }
    }
  }
}