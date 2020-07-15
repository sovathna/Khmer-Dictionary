package com.sovathna.khmerdictionary.ui.words.bookmark

import androidx.fragment.app.viewModels
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.model.state.BookmarksState
import com.sovathna.khmerdictionary.ui.words.AbstractPagingWordsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment :
  AbstractPagingWordsFragment<MviIntent, BookmarksState, BookmarksViewModel>() {

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
    super.render(state)
    with(state) {
      if (isInit) {
        getBookmarksIntent.onNext(BookmarksIntent.GetWords)
      }
      loadSuccess?.getContentIfNotHandled()?.let {
        selectWordIntent.value?.word?.let {
          rv.post {
            selectWordIntent.onNext(WordsIntent.SelectWord(it))
          }
        }
      }
//      clearMenuItemLiveData.value = words?.isNotEmpty() == true
    }
  }

}