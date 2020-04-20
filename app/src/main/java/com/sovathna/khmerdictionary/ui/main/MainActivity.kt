package com.sovathna.khmerdictionary.ui.main

import android.os.Bundle
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.ui.wordlist.WordListFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, WordListFragment(), null)
        .commit()
    }
  }
}
