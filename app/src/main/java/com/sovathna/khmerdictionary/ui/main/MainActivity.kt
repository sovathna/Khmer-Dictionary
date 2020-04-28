package com.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import com.squareup.moshi.JsonClass
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var selectIntent: PublishSubject<WordListIntent.Select>

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
    }
    super.onBackPressed()
  }
}

@JsonClass(generateAdapter = true)
data class Word(
  val _id: Int,
  val word: String,
  val definition: String
)