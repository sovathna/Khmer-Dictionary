package com.sovathna.khmerdictionary.ui.words.search

import androidx.core.view.postDelayed
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.SearchWordsState
import com.sovathna.khmerdictionary.ui.words.AbstractPagingWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchesFragment :
  AbstractPagingWordsFragment<MviIntent, SearchWordsState, SearchesViewModel>() {

  override val viewModel: SearchesViewModel by viewModels()

  @Inject
  lateinit var mainGetWordsIntent: PublishSubject<String>

  override fun onResume() {
    super.onResume()
    viewModel.seaches("")?.observe(viewLifecycleOwner, Observer {
      pagingAdapter.submitData(lifecycle, it)
    })
  }

  override fun intents(): Observable<MviIntent> =
    selectWordIntent.cast(MviIntent::class.java)

  override fun render(state: SearchWordsState) {
    super.render(state)
    with(state) {
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