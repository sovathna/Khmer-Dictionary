package com.sovathna.khmerdictionary.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.data.local.AppDatabase
import com.sovathna.khmerdictionary.data.local.WordEntity
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.nio.charset.Charset
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @SuppressLint("CheckResult")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val db = getDatabasePath("dict.db")
    if(db.exists())
      Log.i("===","exists")
    else Log.i("===","not exists")

//    if (savedInstanceState == null) {
//      supportFragmentManager.beginTransaction()
//        .replace(R.id.fragment_container, WordListFragment(), null)
//        .commit()
//    }
  }
}

@JsonClass(generateAdapter = true)
data class Word(
  val _id: Int,
  val word: String,
  val definition: String
)