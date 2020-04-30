package com.sovathna.khmerdictionary.ui.main

import android.content.res.Configuration
import android.os.Bundle
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.ui.definition.DefinitionFragment
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

//    val db = getDatabasePath("dict.db")
//    if(db.exists())
//      Log.i("===","exists")
//    else Log.i("===","not exists")
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.word_list_container, WordListFragment(), Const.WORD_LIST_FRAGMENT_TAG)
        .commit()
    } else {
      val fragment =
        supportFragmentManager.findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?
      fragment?.let {
        if (supportFragmentManager.backStackEntryCount > 0)
          supportFragmentManager.popBackStackImmediate()

        supportFragmentManager.beginTransaction().replace(
          if (definition_container != null) R.id.definition_container else R.id.word_list_container,
          fragment,
          Const.DEFINITION_FRAGMENT_TAG
        ).addToBackStack(null)
          .commit()
      }

    }
  }

  fun onItemClick(id: Long) {
    definition_container?.let {
      val fragment =
        supportFragmentManager.findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?

      if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStackImmediate()

      supportFragmentManager.beginTransaction().replace(
        R.id.definition_container,
        (fragment ?: DefinitionFragment()).apply {
          arguments = Bundle().apply {
            putLong("id", id)
          }
        },
        Const.DEFINITION_FRAGMENT_TAG
      ).addToBackStack(null)
        .commit()
    } ?: kotlin.run {
      val fragment =
        supportFragmentManager.findFragmentByTag(Const.DEFINITION_FRAGMENT_TAG) as DefinitionFragment?

      if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStackImmediate()

      supportFragmentManager.beginTransaction().replace(
        R.id.word_list_container,
        (fragment ?: DefinitionFragment()).apply {
          arguments = Bundle().apply {
            putLong("id", id)
          }
        },
        Const.DEFINITION_FRAGMENT_TAG
      ).addToBackStack(null)
        .commit()
    }
  }

}

//@JsonClass(generateAdapter = true)
//data class Word(
//  val _id: Int,
//  val word: String,
//  val definition: String
//)