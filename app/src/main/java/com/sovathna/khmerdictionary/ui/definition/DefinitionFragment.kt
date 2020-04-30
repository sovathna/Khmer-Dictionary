package com.sovathna.khmerdictionary.ui.definition

import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.PopupWindowCompat
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.state.DefinitionState
import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_definition.*
import javax.inject.Inject

class DefinitionFragment : MviFragment<DefinitionIntent, DefinitionState, DefinitionViewModel>(
  R.layout.fragment_definition
) {

  @Inject
  lateinit var getDefinitionIntent: PublishSubject<DefinitionIntent.Get>

  @Inject
  lateinit var mActivity: MainActivity

  private var id: Long = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      id = it.getLong("id", 0L)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
      mActivity.title = "ពន្យល់ន័យ"
    } else {
      mActivity.title = getString(R.string.app_name_kh)
    }
    mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)

  }

  override fun onDestroyView() {
    super.onDestroyView()
    mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    mActivity.title = getString(R.string.app_name_kh)
    PopupWindow()
  }

  override fun intents(): Observable<DefinitionIntent> =
    getDefinitionIntent.cast(DefinitionIntent::class.java)

  override fun render(state: DefinitionState) {
    with(state) {
      if (isInit) getDefinitionIntent.onNext(DefinitionIntent.Get(id))
      definition?.let {
        tv_name.text = definition.word

        val tmp = definition.definition.replace("<\"", "<a href=\"")
          .replace("/a", "</a>")
          .replace("\\n", "<br><br>")
          .replace(" : ", " : ឧ. ")
          .replace("ន.", "<span style=\"color:#D50000\">ន.</span>")
          .replace("កិ. វិ.", "<span style=\"color:#D50000\">កិ. វិ.</span>")
          .replace("កិ.វិ.", "<span style=\"color:#D50000\">កិ.វិ.</span>")
          .replace("កិ.", "<span style=\"color:#D50000\">កិ.</span>")
          .replace("និ.", "<span style=\"color:#D50000\">និ.</span>")
          .replace("គុ.", "<span style=\"color:#D50000\">គុ.</span>")
        setTextViewHTML(tv_definition, tmp)
//        tv_definition.text = definition.definition
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
        LogUtil.i("click: ${span?.url}")
      }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
  }

  private fun setTextViewHTML(text: TextView, html: String) {
    val sequence: CharSequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls =
      strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
      makeLinkClickable(strBuilder, span)
    }
    text.text = strBuilder
    text.movementMethod = LinkMovementMethod.getInstance()
  }
}