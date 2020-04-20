package com.sovathna.khmerdictionary.domain.repository

import com.sovathna.khmerdictionary.domain.model.WordList
import io.reactivex.Single

interface DictRepository {
    fun getWordList(): Single<WordList>
}