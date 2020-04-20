package com.sovathna.khmerdictionary.data.repository

import com.sovathna.khmerdictionary.data.local.AppDatabase
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.model.WordList
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(db: AppDatabase) : AppRepository {

  private val wordDao = db.wordDao()

  override fun getWordList(filter: String?, offset: Int): Single<WordList> {
    return when {
      filter != null -> wordDao.getFilterWordList(filter, offset)
      else -> wordDao.getWordList(offset)
    }.map { WordList(it.map { tmp -> Word(tmp.id, tmp.word) }) }
  }

}