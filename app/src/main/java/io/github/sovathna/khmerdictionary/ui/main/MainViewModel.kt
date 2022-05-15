package io.github.sovathna.khmerdictionary.ui.main

import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.BaseViewModel
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import io.github.sovathna.khmerdictionary.ui.detail.DetailState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  appDatabase: AppDatabase,
  private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MainState>(MainState()) {

  private val dao = appDatabase.wordDao()

  fun observeSelectedWord() {
    if (current.isObserved) return
    setState(current.copy(isObserved = true))
    viewModelScope.launch {
      dao.observeWord()
        .distinctUntilChanged()
        .map { entity ->
          if (entity == null) return@map null
          val tmp = entity.definition.replace("<\"", "<a href=\"")
            .replace("/a", "</a>")
            .replace("\\n", "<br><br>")
            .replace(" : ", " : ឧ. ")
            .replace("ន.", "<span style=\"color:#D32F2F\">ន.</span>")
            .replace("កិ. វិ.", "<span style=\"color:#D32F2F\">កិ. វិ.</span>")
            .replace("កិ.វិ.", "<span style=\"color:#D32F2F\">កិ.វិ.</span>")
            .replace("កិ.", "<span style=\"color:#D32F2F\">កិ.</span>")
            .replace("និ.", "<span style=\"color:#D32F2F\">និ.</span>")
            .replace("គុ.", "<span style=\"color:#D32F2F\">គុ.</span>")
          val definition = generate(tmp)
          DetailState(
            id = entity.id,
            word = entity.word,
            definition = definition,
            isBookmark = entity.isBookmark
          )
        }
        .flowOn(ioDispatcher)
        .collectLatest {
          setState(current.copy(detail = it))
        }
    }
  }

  private fun makeLinkClickable(
    strBuilder: SpannableStringBuilder,
    span: URLSpan?
  ) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable = object : ClickableSpan() {
      override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
      }

      override fun onClick(widget: View) {
        Timber.d("url: ${span?.url}")
        span?.url?.toLongOrNull()?.let {
          select(it)
        }
      }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
  }

  private fun generate(html: String): SpannableStringBuilder {
    val sequence: CharSequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls =
      strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
      makeLinkClickable(strBuilder, span)
    }
    return strBuilder
  }

  fun updateBookmark(wordId: Long, isBookmark: Boolean) {
    viewModelScope.launch {
      withContext(ioDispatcher) {
        dao.updateBookmark(wordId, isBookmark)
      }
    }
  }

  fun select(wordId: Long) {
    if (wordId == current.detail?.id) return
    viewModelScope.launch {
      withContext(ioDispatcher) {
        current.detail?.id?.let {
          dao.updateSelection(it, false)
        }
        dao.updateHistory(wordId)
        dao.updateSelection(wordId, true)
      }
    }
  }

}