package com.sovathna.khmerdictionary.ui.definition

import android.os.Bundle
import com.sovathna.androidmvi.fragment.MviFragment
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.state.DefinitionState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_definition.*
import javax.inject.Inject

class DefinitionFragment : MviFragment<DefinitionIntent, DefinitionState, DefinitionViewModel>(
  R.layout.fragment_definition
) {

  @Inject
  lateinit var getDefinitionIntent: PublishSubject<DefinitionIntent.Get>

  private var id: Long = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      id = it.getLong("id", 0L)
    }
  }

  override fun intents(): Observable<DefinitionIntent> =
    getDefinitionIntent.cast(DefinitionIntent::class.java)

  override fun render(state: DefinitionState) {
    with(state) {
      if (isInit) getDefinitionIntent.onNext(DefinitionIntent.Get(id))
      definition?.let {
        tv_name.text = definition.word
        tv_definition.text = definition.definition
      }
    }
  }
}