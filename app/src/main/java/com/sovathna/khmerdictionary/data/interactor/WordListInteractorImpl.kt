package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class WordListInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : WordListInteractor() {

  override fun getWordList() = ObservableTransformer<WordListIntent.Get, WordListResult> {
    it.flatMap { intent ->
      repository.getWordList(intent.filter, intent.offset)
        .toObservable()
        .map(WordListResult::Success)
    }
  }
}