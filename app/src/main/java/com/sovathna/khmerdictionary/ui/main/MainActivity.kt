package com.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.view.MenuItem
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.ui.definition.DefinitionFragment
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var selectIntent: PublishSubject<WordListIntent.Select>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)
    title = getString(R.string.app_name_kh)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        .replace(R.id.word_list_container, WordListFragment(), Const.WORD_LIST_FRAGMENT_TAG)
        .commit()
    } else {
      val fragment =
        supportFragmentManager.findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?
      fragment?.let {
        if (supportFragmentManager.backStackEntryCount > 0)
          supportFragmentManager.popBackStackImmediate()

        val tran = supportFragmentManager.beginTransaction()
        if (definition_container != null)
          tran.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        else
          tran.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
        tran.replace(
          if (definition_container != null) R.id.definition_container else R.id.word_list_container,
          fragment,
          Const.DEFINITION_FRAGMENT_TAG
        ).addToBackStack(null)
          .commit()
      }

    }
  }

  override fun setTitle(title: CharSequence?) {
    super.setTitle(null)
    tv_title?.text = title
  }

  fun onItemClick(id: Long) {
    selectIntent.onNext(WordListIntent.Select(id))
    val fragment =
      supportFragmentManager.findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?

    if (supportFragmentManager.backStackEntryCount > 0)
      supportFragmentManager.popBackStackImmediate()

    val tran = supportFragmentManager.beginTransaction()
    if (definition_container != null)
      tran.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
    else
      tran.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
    tran.replace(
      if (definition_container != null) R.id.definition_container else R.id.word_list_container,
      (fragment ?: DefinitionFragment()).apply {
        arguments = Bundle().apply {
          putLong("id", id)
        }
      },
      Const.DEFINITION_FRAGMENT_TAG
    ).addToBackStack(null)
      .commit()

  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount > 0)
      selectIntent.onNext(WordListIntent.Select(null))
    super.onBackPressed()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

}

//@JsonClass(generateAdapter = true)
//data class Word(
//  val _id: Int,
//  val word: String,
//  val definition: String
//)