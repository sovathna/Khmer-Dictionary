package com.sovathna.khmerdictionary.ui.words

import android.os.Bundle
import android.view.View
import androidx.core.view.postDelayed
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
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

abstract class AbstractWordsFragment<I : MviIntent, S : MviState, VM : BaseViewModel<I, S>> :
  MviFragment<I, S, VM>(R.layout.fragment_word_list) {
  @Inject
  protected lateinit var selectWordIntent: BehaviorSubject<WordsIntent.SelectWord>

  @Inject
  protected lateinit var clickWordSubject: PublishSubject<Event<Word>>

  @Inject
  protected lateinit var recycledViewPool: RecyclerView.RecycledViewPool

  @Inject
  @Named("clear_menu")
  protected lateinit var clearMenuItemLiveData: MutableLiveData<Boolean>

  private lateinit var pagingAdapter: WordItemAdapter
  private lateinit var layoutManager: LinearLayoutManager
  private var loadStates: CombinedLoadStates? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    pagingAdapter = WordItemAdapter()
    pagingAdapter.setOnItemClickListener { _, item ->
      if (!item.isSelected) {
        clickWordSubject.onNext(Event(item.word))
      }
    }
    pagingAdapter.addLoadStateListener {
      loadStates = it
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

  override fun onDestroyView() {
    super.onDestroyView()
    clearMenuItemLiveData.value = false
  }

  protected fun submitData(data: PagingData<WordItem>, hasClearMenu: Boolean? = null) {
    pagingAdapter.submitData(viewLifecycleOwner.lifecycle, data)
    rv.postDelayed(500) {
      val notLoading = loadStates?.prepend is LoadState.NotLoading &&
          loadStates?.refresh is LoadState.NotLoading &&
          loadStates?.append is LoadState.NotLoading
      val isEmpty = (pagingAdapter.itemCount == 0 && notLoading)
      clearMenuItemLiveData.value = !isEmpty && hasClearMenu == true
      if (isEmpty) {
        vs_empty?.inflate()
        view?.findViewById<View>(R.id.layout_empty)?.visibility = View.VISIBLE
      } else {
        view?.findViewById<View>(R.id.layout_empty)?.visibility = View.GONE
      }
    }
  }
}