package com.sovathna.khmerdictionary.domain.repository

import com.sovathna.khmerdictionary.domain.model.WordList
import io.reactivex.Single

interface AppRepository {
  fun getWordList(filter: String?, offset: Int): Single<WordList>
}