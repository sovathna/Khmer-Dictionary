package com.sovathna.khmerdictionary.ui.words

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.state.MviState
import com.sovathna.androidmvi.viewmodel.BaseViewModel
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import javax.inject.Inject
import javax.inject.Named

abstract class AbstractPagingWordsFragment<I : MviIntent, S : MviState, VM : BaseViewModel<I, S>> :
  MviFragment<I, S, VM>(
    R.layout.fragment_word_list
  ) {
  @Inject
  protected lateinit var selectWordIntent: BehaviorSubject<WordsIntent.SelectWord>

  @Inject
  protected lateinit var clickWordSubject: PublishSubject<Event<Word>>

  @Inject
  protected lateinit var recycledViewPool: RecyclerView.RecycledViewPool

  @Inject
  @Named("clear_menu")
  protected lateinit var clearMenuItemLiveData: MutableLiveData<Boolean>

  protected lateinit var pagingAdapter: WordsPagingAdapter
  private lateinit var layoutManager: LinearLayoutManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    pagingAdapter = WordsPagingAdapter()
    pagingAdapter.setOnItemClickListener { _, item ->
      if (!item.isSelected) {
        clickWordSubject.onNext(Event(item.word))
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rv.setRecycledViewPool(recycledViewPool)
    rv.setHasFixedSize(true)

    layoutManager = LinearLayoutManager(requireContext())
    rv.layoutManager = layoutManager
    rv.adapter = pagingAdapter
  }

  override fun onPause() {
    super.onPause()
    clearMenuItemLiveData.value = false
  }

  override fun render(state: S) {

  }
}