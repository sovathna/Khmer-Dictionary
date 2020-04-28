package com.sovathna.khmerdictionary.domain.interactor

import com.sovathna.androidmvi.MviInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.result.DefinitionResult
import io.reactivex.ObservableTransformer

abstract class DefinitionInteractor : MviInteractor<DefinitionIntent, DefinitionResult>() {

  abstract val getDefinition: ObservableTransformer<DefinitionIntent.Get, DefinitionResult>

  override val intentsProcessor = ObservableTransformer<DefinitionIntent, DefinitionResult> {
    it.publish { intent ->
      intent.ofType(DefinitionIntent.Get::class.java).compose(getDefinition)
    }
  }
}