package com.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import android.util.Log
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import com.squareup.moshi.JsonClass
import dagger.android.support.DaggerAppCompatActivity

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
        .replace(R.id.fragment_container, WordListFragment(), null)
        .commit()
    }
  }
}

@JsonClass(generateAdapter = true)
data class Word(
  val _id: Int,
  val word: String,
  val definition: String
)