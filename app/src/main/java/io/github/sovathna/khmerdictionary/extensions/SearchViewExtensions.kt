package io.github.sovathna.khmerdictionary.extensions

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val SearchView.queryTextChangedFlow: Flow<String>
  get() = callbackFlow {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
          trySendBlocking(newText)
        }
        return false
      }
    })
    awaitClose {
      setOnQueryTextListener(null)
    }
  }