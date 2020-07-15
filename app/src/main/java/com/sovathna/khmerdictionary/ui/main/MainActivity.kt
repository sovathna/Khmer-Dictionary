package com.sovathna.khmerdictionary.ui.main

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.livedata.EventObserver
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.listener.DrawerListener
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.intent.BookmarksIntent
import com.sovathna.khmerdictionary.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.model.intent.SearchesIntent
import com.sovathna.khmerdictionary.model.intent.WordsIntent
import com.sovathna.khmerdictionary.ui.definition.DefinitionActivity
import com.sovathna.khmerdictionary.ui.definition.fragment.DefinitionFragment
import com.sovathna.khmerdictionary.ui.words.bookmark.BookmarksFragment
import com.sovathna.khmerdictionary.ui.words.history.HistoriesFragment
import com.sovathna.khmerdictionary.ui.words.main.WordsFragment
import com.sovathna.khmerdictionary.ui.words.search.SearchesFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  @Inject
  lateinit var searchesIntent: PublishSubject<SearchesIntent.GetWords>

  @Inject
  lateinit var clearHistoriesIntent: PublishSubject<HistoriesIntent.ClearHistories>

  @Inject
  lateinit var clearBookmarksIntent: PublishSubject<BookmarksIntent.ClearBookmarks>

  @Inject
  lateinit var clickWordSubject: PublishSubject<Event<Word>>

  @Inject
  lateinit var fabVisibilitySubject: PublishSubject<Boolean>

  @Inject
  lateinit var selectWordSubject: BehaviorSubject<WordsIntent.SelectWord>

  @Inject
  lateinit var bookmarkedLiveData: MutableLiveData<Boolean>

  @Inject
  lateinit var menuItemClickLiveData: MutableLiveData<Event<String>>

  @Inject
  @Named("clear_menu")
  lateinit var clearMenuItemLiveData: MutableLiveData<Boolean>

  private val viewModel: MainViewModel by viewModels()

  private var menu: Menu? = null
  private var searchItem: MenuItem? = null
  private var closeDialog: AlertDialog? = null
  private var clearDialog: AlertDialog? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

//    FirebaseInstanceId.getInstance().instanceId
//      .addOnCompleteListener(OnCompleteListener { task ->
//        if (!task.isSuccessful) {
//          Logger.e("getInstanceId failed")
//          return@OnCompleteListener
//        }
//
//        val token = task.result?.token
//        Logger.d("FCM Token: $token")
//      })

    setSupportActionBar(toolbar)

    bookmarkedLiveData.observe(this, bookmarkMenuItemObserver)

    LiveDataReactiveStreams.fromPublisher(
      clickWordSubject
        .throttleFirst(200, TimeUnit.MILLISECONDS)
        .replay(1)
        .autoConnect(0)
        .toFlowable(BackpressureStrategy.BUFFER)
    ).observe(this, EventObserver(::onItemClickObserver))

    fab.setOnClickListener(fabClickListener)

    nav_view.setNavigationItemSelectedListener(navMenuItemClickListener)
    drawer_layout.addDrawerListener(object : DrawerListener() {
      override fun onDrawerOpened(drawerView: View) {
        if (searchItem?.isActionViewExpanded == true) {
          searchItem?.collapseActionView()
        }
      }
    })

    LiveDataReactiveStreams.fromPublisher(
      fabVisibilitySubject.toFlowable(BackpressureStrategy.BUFFER)
    ).observe(this, Observer { if (it) fab.show() else fab.hide() })

    clearMenuItemLiveData.observe(this, Observer {
      menu?.findItem(R.id.action_clear)?.isVisible = it
    })

    viewModel.titleLiveData.observe(this, Observer {
      title = it
      nav_view.checkedItem?.isChecked = it != getString(R.string.app_name_kh)

    })

    if (savedInstanceState == null) {
      viewModel.title = getString(R.string.app_name_kh)
      supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        .replace(
          R.id.word_list_container,
          WordsFragment(),
          Const.WORD_LIST_FRAGMENT_TAG
        )
        .commit()
    } else {
      supportFragmentManager
        .findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG)?.let {
          it.arguments?.let { args ->
            args.getParcelable<Word>("word")?.let { word ->
              clickWordSubject.onNext(Event(word))
            }
          }
          supportFragmentManager
            .beginTransaction()
            .remove(it)
            .commit()
        }
    }

  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    val drawerToggle = ActionBarDrawerToggle(
      this,
      drawer_layout,
      toolbar,
      R.string.nav_open,
      R.string.nav_close
    )
    drawerToggle.syncState()
  }

  override fun setTitle(title: CharSequence?) {
    super.setTitle(null)
    tv_title?.text = title
  }

  private fun setDefMenuItemsVisible(isVisible: Boolean) {
    menu?.setGroupVisible(R.id.group_def, isVisible)
  }

  private val bookmarkMenuItemObserver = Observer<Boolean> { isBookmark ->
    menu?.findItem(R.id.action_bookmark)?.let { item ->
      when {
        isBookmark -> {
          item.setTitle(R.string.delete_bookmark)
          item.icon = ContextCompat.getDrawable(
            this,
            R.drawable.round_bookmark_white_24
          )
        }
        else -> {
          item.setTitle(R.string.add_bookmark)
          item.icon = ContextCompat.getDrawable(
            this,
            R.drawable.round_bookmark_border_white_24
          )
        }
      }
    }
  }

  private val navMenuItemClickListener =
    NavigationView.OnNavigationItemSelectedListener { menu ->
      drawer_layout.closeDrawer(GravityCompat.START)
      if (!menu.isChecked) {
        menu.isChecked = true
        if (supportFragmentManager.backStackEntryCount > 0) {
          super.onBackPressed()
        }
        when (menu.itemId) {
          R.id.nav_histories -> {
            viewModel.title = getString(R.string.histories)
            supportFragmentManager.beginTransaction()
              .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
              )
              .replace(
                R.id.word_list_container,
                HistoriesFragment(),
                Const.HISTORIES_FRAGMENT_TAG
              )
              .addToBackStack(null)
              .commit()
          }
          R.id.nav_bookmarks -> {
            viewModel.title = getString(R.string.bookmarks)
            supportFragmentManager.beginTransaction()
              .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
              )
              .replace(
                R.id.word_list_container,
                BookmarksFragment(),
                Const.BOOKMARKS_FRAGMENT_TAG
              )
              .addToBackStack(null)
              .commit()
          }
        }
      }
      true
    }

  private val fabClickListener = View.OnClickListener {
    if (searchItem?.isActionViewExpanded == false) {
      if (supportFragmentManager.backStackEntryCount > 0) {
        viewModel.title = getString(R.string.app_name_kh)
        super.onBackPressed()
      }
      searchItem?.expandActionView()
      supportFragmentManager.beginTransaction()
        .setCustomAnimations(
          R.anim.fade_in,
          R.anim.fade_out,
          R.anim.fade_in,
          R.anim.fade_out
        )
        .replace(
          R.id.word_list_container,
          SearchesFragment(),
          Const.SEARCH_WORDS_FRAGMENT_TAG
        )
        .addToBackStack(null)
        .commit()
    } else {
      val searchView = searchItem?.actionView as? SearchView
      searchView?.let {
        if (it.query.isNotEmpty()) it.setQuery("", false)
        else searchItem?.collapseActionView()
      }
    }
  }

  private fun onItemClickObserver(word: Word) {
    selectWordSubject.onNext(WordsIntent.SelectWord(word))
    setDefMenuItemsVisible(definition_container != null && selectWordSubject.value?.word != null)
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
      val intent = Intent(this, DefinitionActivity::class.java)
      intent.putExtra("word", word)
      startActivityForResult(intent, 0)
    } else {
      supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
          R.anim.fade_in,
          R.anim.fade_out
        )
        .replace(
          R.id.definition_container,
          DefinitionFragment().apply {
            arguments = Bundle().apply {
              putParcelable("word", word)
            }
          },
          Const.DEFINITION_FRAGMENT_TAG
        )
        .commit()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 0) {
      if (resultCode == Activity.RESULT_OK) {
        data?.let {
          it.getParcelableExtra<Word>("word")?.let { word ->
            clickWordSubject.onNext(Event(word))
          }
        }
      } else if (resultCode == Activity.RESULT_CANCELED) {
        selectWordSubject.onNext(WordsIntent.SelectWord(null))
        setDefMenuItemsVisible(false)
      }
    }
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      if (supportFragmentManager.backStackEntryCount > 0) {
        viewModel.title = getString(R.string.app_name_kh)
        super.onBackPressed()
      } else {
        val defTmp = supportFragmentManager.findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG)
        if (defTmp != null) {
          setDefMenuItemsVisible(false)
          supportFragmentManager
            .beginTransaction()
            .remove(defTmp)
            .commit()
          selectWordSubject.onNext(WordsIntent.SelectWord(null))
        } else {
          showCloseDialog()
        }
      }
    }
  }

  override fun onPause() {
    super.onPause()
    closeDialog?.dismiss()
    clearDialog?.dismiss()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    this.menu = menu

    // Set clear menu item based on which fragment has it
    menu?.findItem(R.id.action_clear)?.isVisible = clearMenuItemLiveData.value == true

    // Set definition menu items visibility
    setDefMenuItemsVisible(definition_container != null && selectWordSubject.value?.word != null)

    searchItem = menu?.findItem(R.id.action_search)
    searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
      override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        fab?.setImageDrawable(
          ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.round_clear_white_24
          )
        )
        return true
      }

      override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

        fab?.setImageDrawable(
          ContextCompat.getDrawable(
            this@MainActivity,
            R.drawable.round_search_white_24
          )
        )
        if (supportFragmentManager.backStackEntryCount > 0) {
          supportFragmentManager.popBackStack()
        }
        viewModel.title = getString(R.string.app_name_kh)
        return true
      }
    })

    val searchView = searchItem!!.actionView as SearchView
    searchView.queryHint = getString(R.string.search_hint)
    supportFragmentManager.findFragmentByTag(Const.SEARCH_WORDS_FRAGMENT_TAG)?.let {
      searchItem?.expandActionView()
      searchView.setQuery(viewModel.searchTerm, true)
    }
    searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)?.apply {
      textSize = 14.0F
      typeface = ResourcesCompat.getFont(this@MainActivity, R.font.kantumruy)
    }

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextChange(newText: String?): Boolean {

        val searchTerm = newText?.trim() ?: ""
        if (searchTerm != viewModel.searchTerm) {
          viewModel.searchTerm = searchTerm
          searchesIntent.onNext(
            SearchesIntent.GetWords(
              viewModel.searchTerm
            )
          )
        }
        return true
      }

      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }
    })
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.action_bookmark) {
      menuItemClickLiveData.value = Event("bookmark")
    } else if (item.itemId == R.id.action_zoom_in) {
      menuItemClickLiveData.value = Event("zoom_in")
    } else if (item.itemId == R.id.action_zoom_out) {
      menuItemClickLiveData.value = Event("zoom_out")
    } else if (item.itemId == R.id.action_clear) {
      showClearDialog()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun showCloseDialog() {
    val builder = AlertDialog.Builder(this)
    val v = LayoutInflater.from(this)
      .inflate(R.layout.dialog_clear_words, null, false)
    v.findViewById<AppCompatTextView>(R.id.tv_title)?.setText(R.string.close_title)
    v.findViewById<AppCompatTextView>(R.id.tv_description)?.setText(R.string.close_message)
    v.findViewById<AppCompatButton>(R.id.btn_cancel)?.setOnClickListener {
      closeDialog?.dismiss()
    }
    v.findViewById<AppCompatButton>(R.id.btn_clear)?.let {
      it.setOnClickListener {
        closeDialog?.dismiss()
        finish()
      }
    }
    builder.setView(v)
    closeDialog = builder.show()

  }

  private fun showClearDialog() {
    val builder = AlertDialog.Builder(this)
    val v = LayoutInflater.from(this)
      .inflate(R.layout.dialog_clear_words, null, false)
    v.findViewById<AppCompatButton>(R.id.btn_cancel)?.setOnClickListener {
      clearDialog?.dismiss()
    }
    v.findViewById<AppCompatButton>(R.id.btn_clear)?.setOnClickListener {
      clearDialog?.dismiss()
      if (viewModel.title == getString(R.string.histories)) {
        clearHistoriesIntent.onNext(HistoriesIntent.ClearHistories)
      } else if (viewModel.title == getString(R.string.bookmarks)) {
        clearBookmarksIntent.onNext(BookmarksIntent.ClearBookmarks)
        bookmarkedLiveData.value = false
      }

    }
    builder.setView(v)
    clearDialog = builder.show()
  }

}

//@JsonClass(generateAdapter = true)
//data class Word(
//  val _id: Int,
//  val word: String,
//  val definition: String
//)