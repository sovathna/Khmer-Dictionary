package com.sovathna.khmerdictionary.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import com.sovathna.khmerdictionary.util.LogUtil
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var selectIntent: PublishSubject<WordListIntent.Select>

  @Inject
  lateinit var filterIntent: PublishSubject<WordListIntent.Filter>

  var searchItem: MenuItem? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

//    val db = getDatabasePath("dict.db")
//    if(db.exists())
//      Log.i("===","exists")
//    else Log.i("===","not exists")

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, WordListFragment(), "word_list_fragment")
        .commit()
    }
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 0) {
      selectIntent.onNext(WordListIntent.Select(null))
      searchItem?.isVisible = true
    }
    super.onBackPressed()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    searchItem = menu.findItem(R.id.action_search)
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
      searchItem?.isVisible = supportFragmentManager.backStackEntryCount <= 0
    } else {
      searchItem?.isVisible = true
    }

    val searchView = searchItem!!.actionView as SearchView
    searchView.queryHint = "ស្វែងរកពាក្យ"
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {
        LogUtil.i("query text changed: $newText")
        val searchTerm = newText?.trim()
        filterIntent.onNext(
          WordListIntent.Filter(searchTerm, 0)
        )
        return true
      }

      override fun onQueryTextSubmit(query: String?): Boolean {
        val searchTerm = query?.trim()
        filterIntent.onNext(
          WordListIntent.Filter(searchTerm, 0)
        )
        return true
      }
    })
    return true
  }
}

//@JsonClass(generateAdapter = true)
//data class Word(
//  val _id: Int,
//  val word: String,
//  val definition: String
//)