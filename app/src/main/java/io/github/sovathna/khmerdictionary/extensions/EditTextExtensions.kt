package io.github.sovathna.khmerdictionary.extensions

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val EditText.textChangedFlow: Flow<String?>
  get() = callbackFlow {
    val watcher = addTextChangedListener {
      trySendBlocking(it?.toString())
    }
    awaitClose {
      removeTextChangedListener(watcher)
    }
  }