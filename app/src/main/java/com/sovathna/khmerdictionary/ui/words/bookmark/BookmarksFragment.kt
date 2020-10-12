package com.sovathna.khmerdictionary.ui.words.bookmark

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.words.AbstractWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment :
  AbstractWordsFragment<MviIntent, BookmarksState, BookmarksViewModel>() {

  override val viewModel: BookmarksViewModel by viewModels()

  private val getBookmarksIntent = PublishSubject.create<BookmarksIntent.GetWords>()

  @Inject
  lateinit var clearBookmarksIntent: PublishSubject<BookmarksIntent.ClearBookmarks>

  override fun intents(): Observable<MviIntent> =
    Observable.merge(
      getBookmarksIntent,
      selectWordIntent,
      clearBookmarksIntent
    )

  override fun render(state: BookmarksState) {
    with(state) {
      wordsLiveData?.observe(viewLifecycleOwner, Observer {
        submitData(it, true)
      })

      if (isInit) getBookmarksIntent.onNext(BookmarksIntent.GetWords)

      loadSuccess?.getContentIfNotHandled()?.let {
        selectWordIntent.value?.word?.let {
          rv.post { selectWordIntent.onNext(WordsIntent.SelectWord(it)) }
        }
      }
    }
  }

}