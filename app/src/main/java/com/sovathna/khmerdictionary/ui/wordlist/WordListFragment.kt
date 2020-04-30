package com.sovathna.khmerdictionary.ui.wordlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.state.WordListState
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
  @Named("filter")
  lateinit var filterIntent: PublishSubject<WordListIntent.Filter>

  @Inject
  @Named("search")
  lateinit var searchIntent: PublishSubject<WordListIntent.Filter>

  @Inject
  lateinit var selectIntent: PublishSubject<WordListIntent.Select>

  @Inject
  lateinit var adapter: WordListAdapter

  @Inject
  lateinit var layoutManagerProvider: Provider<RecyclerView.LayoutManager>

  @Inject
  lateinit var mActivity: MainActivity

  lateinit var layoutManager: LinearLayoutManager

  private var scrollChanged: ViewTreeObserver.OnScrollChangedListener? = null

  private var searchItem: MenuItem? = null

  private var oldTerm: String? = ""
  private var searchItemState: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    LogUtil.i("onCreate")
    if (savedInstanceState != null) {
      oldTerm = savedInstanceState.getString("old_term")
      searchItemState = savedInstanceState.getBoolean("search_item_state")
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    layoutManager = layoutManagerProvider.get() as LinearLayoutManager
    rv.layoutManager = layoutManager
    rv.adapter = adapter
    LogUtil.i("onViewCreated")
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    LogUtil.i("onSaveInstanceState")
    outState.putString("old_term", oldTerm)
    outState.putBoolean("search_item_state", searchItemState)
  }

  override fun onPause() {
    searchItemState = searchItem?.isActionViewExpanded == true
    super.onPause()
  }

  override fun intents(): Observable<WordListIntent> =
    Observable.merge(
      filterIntent,
      selectIntent,
      searchIntent.throttleLast(1000, TimeUnit.MILLISECONDS)
    )

  override fun render(state: WordListState) {
    with(state) {
//      LogUtil.i("state: $this")

      LogUtil.i("size: ${words?.size}")

      if (isInit) filterIntent.onNext(WordListIntent.Filter(null, 0))


      if (isMore) {
        addScrollChangedListener(words?.size ?: 0)
      } else {
        removeScrollChangedListener()
      }

      if (words?.isNotEmpty() == true) {
        adapter.setOnItemClickListener { index ->
          if (!adapter.currentList[index].isSelected) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
              (searchItem?.actionView as? SearchView)?.setOnQueryTextListener(null)
//            searchItemState = searchItem?.isActionViewExpanded == true
            mActivity.onItemClick(adapter.currentList[index].word.id)
          }
        }
      } else {
        adapter.setOnItemClickListener(null)
      }

      adapter.submitList(words)

      resetEvent?.getContentIfNotHandled()?.let {
        rv.postDelayed({
          rv.smoothScrollToPosition(0)
        }, 200)
      }

    }

  }

  private fun addScrollChangedListener(itemCount: Int) {
    removeScrollChangedListener()
    scrollChanged = ViewTreeObserver.OnScrollChangedListener {
      if (layoutManager.findLastVisibleItemPosition() + Const.LOAD_MORE_THRESHOLD >= itemCount) {
        filterIntent.onNext(WordListIntent.Filter(null, itemCount))
        LogUtil.i("load more")
        removeScrollChangedListener()
      }
    }
    rv?.viewTreeObserver?.addOnScrollChangedListener(scrollChanged)
  }

  private fun removeScrollChangedListener() {
    scrollChanged?.let {
      rv?.viewTreeObserver?.removeOnScrollChangedListener(it)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.main, menu)
    LogUtil.i("onCreateOptionsMenu $oldTerm")
    searchItem = menu.findItem(R.id.action_search)
    if (searchItemState) searchItem?.expandActionView()
    val searchView = searchItem!!.actionView as SearchView
    searchView.queryHint = "ស្វែងរកពាក្យ"
    searchView.setQuery(oldTerm, false)
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {
        LogUtil.i("query text changed: $newText")
        val searchTerm = newText?.trim()
        if (searchTerm != oldTerm) {
          LogUtil.i("search: $searchTerm")
          oldTerm = searchTerm
          filterIntent.onNext(
            WordListIntent.Filter(searchTerm, 0)
          )
          return true
        }
        return false
      }

      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }
    })
  }

}