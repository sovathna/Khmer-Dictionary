package io.github.sovathna.khmerdictionary.ui.detail

import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sovathna.khmerdictionary.data.AppSettings
import io.github.sovathna.khmerdictionary.data.db.DictDao
import io.github.sovathna.khmerdictionary.data.db.LocalDao
import io.github.sovathna.khmerdictionary.model.entity.DictEntity
import io.github.sovathna.khmerdictionary.model.entity.HistoryEntity
import io.github.sovathna.khmerdictionary.ui.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
  private val settings: AppSettings,
  private val dictDao: DictDao,
  private val localDao: LocalDao
) : BaseViewModel<DetailState>(DetailState(fontSize = runBlocking { settings.getFontSize() })) {

  init {
    init()
  }

  private fun init() {
    viewModelScope.launch {
      settings.detailIdFlow
        .distinctUntilChanged()
        .collectLatest {
          getDetail(it)
        }
    }
  }

  private fun getDetail(id: Long) {
    viewModelScope.launch {
      dictDao.get(id)?.let { dict ->
        setState(
          current.copy(
            id = dict.id,
            word = dict.word,
            definition = generate(dict.definition),
            fontSize = settings.getFontSize()
          )
        )
        addHistory(dict)
      }
    }
  }

  private fun addHistory(dict: DictEntity) {
    viewModelScope.launch {
      try {
        Timber.tag("debug").d("$dict")
        localDao.addHistory(HistoryEntity(dict.id, dict.word))
      } catch (e: Exception) {
        Timber.tag("debug").e(e)
      }
    }
  }


  fun select(id: Long) {
    if (id == current.id) return
    viewModelScope.launch {
      settings.setDetailId(id)
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
        span?.url?.toLongOrNull()?.let { select(it) }
      }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
  }

  private fun generate(def: String): SpannableStringBuilder {
    val html = def.replace("<\"", "<a href=\"")
      .replace("/a", "</a>")
      .replace("\\n", "<br><br>")
      .replace(" : ", " : ឧ. ")
      .replace("ន.", "<span style=\"color:#D32F2F\">ន.</span>")
      .replace("កិ. វិ.", "<span style=\"color:#D32F2F\">កិ. វិ.</span>")
      .replace("កិ.វិ.", "<span style=\"color:#D32F2F\">កិ.វិ.</span>")
      .replace("កិ.", "<span style=\"color:#D32F2F\">កិ.</span>")
      .replace("និ.", "<span style=\"color:#D32F2F\">និ.</span>")
      .replace("គុ.", "<span style=\"color:#D32F2F\">គុ.</span>")
    val sequence: CharSequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls =
      strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
      makeLinkClickable(strBuilder, span)
    }
    return strBuilder
  }
}