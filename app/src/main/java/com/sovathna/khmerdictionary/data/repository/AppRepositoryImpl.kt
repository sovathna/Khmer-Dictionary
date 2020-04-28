package com.sovathna.khmerdictionary.data.repository

import com.sovathna.khmerdictionary.data.local.AppDatabase
import com.sovathna.khmerdictionary.domain.model.Definition
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(db: AppDatabase) : AppRepository {

  private val wordDao = db.wordDao()

  override fun getWordList(filter: String?, offset: Int): Single<List<Word>> {
    return when {
      filter.isNullOrEmpty() -> wordDao.getWordList(offset)
      else -> wordDao.getFilterWordList("$filter%", offset)
    }.map { it.map { tmp -> Word(tmp.id, tmp.word) } }
  }

  override fun getDefinition(id: Long): Single<Definition> {
    return wordDao.getDefinition(id).map { Definition(it.word, it.definition) }
  }
}