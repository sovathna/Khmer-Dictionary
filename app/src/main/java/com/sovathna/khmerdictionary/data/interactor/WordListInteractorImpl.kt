package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.WordListInteractor
import com.sovathna.khmerdictionary.domain.model.intent.WordListIntent
import com.sovathna.khmerdictionary.domain.model.result.WordListResult
import com.sovathna.khmerdictionary.domain.repository.DictRepository
import io.reactivex.ObservableTransformer

class WordListInteractorImpl(
    private val repository: DictRepository
) : WordListInteractor() {
    override fun getWordList() = ObservableTransformer<WordListIntent.Get, WordListResult> {
        it.flatMap {
            repository.getWordList()
                .toObservable()
                .map(WordListResult::Success)
                .cast(WordListResult::class.java)
        }
    }
}