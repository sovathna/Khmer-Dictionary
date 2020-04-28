package com.sovathna.khmerdictionary.domain.repository

import com.sovathna.khmerdictionary.domain.model.Definition
import com.sovathna.khmerdictionary.domain.model.Word
import io.reactivex.Single

interface AppRepository {
  fun getWordList(filter: String?, offset: Int): Single<List<Word>>

  fun getDefinition(id: Long): Single<Definition>
}