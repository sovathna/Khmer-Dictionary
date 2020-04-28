package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.DefinitionInteractor
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.result.DefinitionResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefinitionInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : DefinitionInteractor() {

  override val getDefinition = ObservableTransformer<DefinitionIntent.Get, DefinitionResult> {
    it.flatMap { intent ->
      repository.getDefinition(intent.id)
        .toObservable()
        .map(DefinitionResult::Success)
        .subscribeOn(Schedulers.io())
    }
  }
}