package com.sovathna.khmerdictionary.ui.definition.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.Logger
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.livedata.EventObserver
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.data.local.pref.AppPreferences
import com.sovathna.khmerdictionary.model.Definition
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.model.state.DefinitionState
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_definition.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class DefinitionFragment :
  MviFragment<DefinitionIntent, DefinitionState, DefinitionViewModel>(
    R.layout.fragment_definition
  ) {

  override val viewModel: DefinitionViewModel by viewModels()

  private val getDefinitionIntent =
    PublishSubject.create<DefinitionIntent.GetDefinition>()
  private val getQuickDefinitionIntent =
    PublishSubject.create<DefinitionIntent.GetQuickDefinition>()
  private val addDeleteBookmarkIntent =
    PublishSubject.create<DefinitionIntent.AddDeleteBookmark>()

  @Inject
  lateinit var fabVisibilitySubject: Lazy<PublishSubject<Boolean>>

  @Inject
  lateinit var bookmarkedLiveData: MutableLiveData<Boolean>

  @Inject
  lateinit var menuItemClickLiveData: MutableLiveData<Event<String>>

  @Inject
  lateinit var appPref: AppPreferences

  private lateinit var word: Word

  private var isRendered = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      word = it.getParcelable("word")!!
      fabVisibilitySubject.get().onNext(true)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val textSize = appPref.getTextSize()
    tv_definition.textSize = textSize
    tv_name.textSize = textSize + 8.0F

    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      nsv.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
        if (scrollY < oldScrollY) {
          fabVisibilitySubject.get().onNext(true)
        } else if (scrollY > oldScrollY) {
          fabVisibilitySubject.get().onNext(false)
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    menuItemClickLiveData.observe(viewLifecycleOwner, EventObserver {
      when (it) {
        "bookmark" -> {
          addDeleteBookmarkIntent.onNext(DefinitionIntent.AddDeleteBookmark(word))
        }
        "zoom_in" -> {
          val textSize = appPref.incrementTextSize()
          tv_definition.textSize = textSize
          tv_name.textSize = textSize + 8.0F
        }
        "zoom_out" -> {
          val textSize = appPref.decrementTextSize()
          tv_definition.textSize = textSize
          tv_name.textSize = textSize + 8.0F
        }
      }
    })
  }

  override fun onPause() {
    super.onPause()
    quickDefDialog?.dismiss()
  }

  override fun intents(): Observable<DefinitionIntent> =
    Observable.merge(
      getDefinitionIntent,
      getQuickDefinitionIntent.throttleFirst(200, TimeUnit.MILLISECONDS),
      addDeleteBookmarkIntent
    )

  override fun render(state: DefinitionState) {
    with(state) {
      if (isInit) getDefinitionIntent.onNext(DefinitionIntent.GetDefinition(word))

      definition?.let {
        if (!isRendered) {
          isRendered = true
          tv_name.text = definition.word
          setTextViewHTML(tv_definition, definition.definition)
        }
      }

      isBookmark?.let {
        bookmarkedLiveData.value = it
      }

      quickDef?.getContentIfNotHandled()?.let {
        showQuickDefDialog(it)
      }
    }
  }

  private var quickDefDialog: AlertDialog? = null

  private fun showQuickDefDialog(def: Definition) {
    val builder = AlertDialog.Builder(requireContext())
    val v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_definition, null, false)
    v.findViewById<AppCompatTextView>(R.id.tv_name)?.let {
      it.text = def.word
    }
    v.findViewById<AppCompatTextView>(R.id.tv_definition)?.let {
//      it.text = def.definition
      setQuickTextViewHTML(it, def.definition)
      it.movementMethod = ScrollingMovementMethod.getInstance()
    }
    builder.setView(v)
    quickDefDialog = builder.show()
//    quickDefDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
//    quickDefDialog?.show()
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
        Logger.d("click: ${span?.url}")
        span?.url?.let {
          getQuickDefinitionIntent.onNext(DefinitionIntent.GetQuickDefinition(it.toLong()))
        }
      }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
  }

  private fun setQuickTextViewHTML(text: TextView, html: String) {
    val sequence: CharSequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls =
      strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
      removeUnderline(strBuilder, span)
    }
    text.text = strBuilder
  }

  private fun removeUnderline(
    strBuilder: SpannableStringBuilder,
    span: URLSpan?
  ) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable = object : UnderlineSpan() {
      override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
      }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
  }

}