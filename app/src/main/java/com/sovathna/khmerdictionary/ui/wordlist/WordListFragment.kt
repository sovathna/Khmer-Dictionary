package com.sovathna.khmerdictionary.ui.wordlist

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.state.WordListState
import com.sovathna.khmerdictionary.ui.definition.DefinitionFragment
import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_word_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class WordListFragment : MviFragment<WordListIntent, WordListState, WordListViewModel>(
  R.layout.fragment_word_list
) {

  @Inject
  @Named("word_list")
  lateinit var filterWordListIntent: PublishSubject<WordListIntent.Filter>

  @Inject
  lateinit var filterIntent: PublishSubject<WordListIntent.Filter>

  @Inject
  lateinit var selectIntent: PublishSubject<WordListIntent.Select>

  @Inject
  lateinit var adapter: WordListAdapter

  @Inject
  lateinit var layoutManager: Provider<RecyclerView.LayoutManager>

  @Inject
  lateinit var mActivity: MainActivity

  private var scrollChanged: ViewTreeObserver.OnScrollChangedListener? = null

  private fun replaceDefinitionFragment(id: Long) {
    mActivity.searchItem?.isVisible = false
    requireActivity().supportFragmentManager.beginTransaction().replace(
      R.id.definition_container,
      DefinitionFragment().apply {
        arguments = Bundle().apply {
          putLong("id", id)
        }
      },
      "definition_fragment"
    ).addToBackStack(null)
      .commit()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rv.layoutManager = layoutManager.get()
    rv.adapter = adapter
  }

  override fun intents(): Observable<WordListIntent> =
    Observable.merge(
      filterWordListIntent
        .throttleFirst(500, TimeUnit.MILLISECONDS),
      selectIntent,
      filterIntent.throttleLast(1000, TimeUnit.MILLISECONDS)
    )

  override fun render(state: WordListState) {
    with(state) {
//      LogUtil.i("state: $this")

      LogUtil.i("size: ${words?.size}")

      if (isInit) filterWordListIntent.onNext(WordListIntent.Filter(null, 0))


      if (isMore) {
        addScrollChangedListener(words?.size ?: 0)
      } else {
        removeScrollChangedListener()
      }

      if (words?.isNotEmpty() == true) {
        adapter.setOnItemClickListener { index ->
          if (!adapter.currentList[index].isSelected) {
            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
              if (requireActivity().supportFragmentManager.popBackStackImmediate()) {
                selectIntent.onNext(WordListIntent.Select(index))
                replaceDefinitionFragment(adapter.currentList[index].word.id)
              }
            } else {
              selectIntent.onNext(WordListIntent.Select(index))
              replaceDefinitionFragment(adapter.currentList[index].word.id)
            }
          }
        }
      } else {
        adapter.setOnItemClickListener(null)
      }

      adapter.submitList(words)

      resetEvent?.getContentIfNotHandled()?.let {
        rv.layoutManager?.postOnAnimation {
          rv.scrollToPosition(0)
        }
      }
    }

  }

  private fun addScrollChangedListener(itemCount: Int) {
    removeScrollChangedListener()
    scrollChanged = ViewTreeObserver.OnScrollChangedListener {
      val tmp = rv.layoutManager
      if (tmp is LinearLayoutManager) {
        if (tmp.findLastVisibleItemPosition() + Const.LOAD_MORE_THRESHOLD >= itemCount) {
          filterWordListIntent.onNext(WordListIntent.Filter(null, itemCount))
          LogUtil.i("load more")
          removeScrollChangedListener()
        }
      }
    }
    rv?.viewTreeObserver?.addOnScrollChangedListener(scrollChanged)
  }

  private fun removeScrollChangedListener() {
    scrollChanged?.let {
      rv?.viewTreeObserver?.removeOnScrollChangedListener(it)
    }
  }

}