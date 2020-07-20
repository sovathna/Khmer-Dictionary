package com.sovathna.khmerdictionary.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.sovathna.androidmvi.Logger
import com.sovathna.khmerdictionary.data.local.db.AppDatabase
import com.sovathna.khmerdictionary.data.local.db.LocalDatabase
import com.sovathna.khmerdictionary.model.entity.WordUI
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

@OptIn(ExperimentalPagingApi::class)
class WordsRemoteMediator(
  db: AppDatabase,
  local: LocalDatabase
) : RxRemoteMediator<Int, WordUI>() {

  private val dao = db.wordDao()
  private val uiDao = local.wordUIDao()

  override fun loadSingle(
    loadType: LoadType,
    state: PagingState<Int, WordUI>
  ): Single<MediatorResult> {
    return when (loadType) {
      LoadType.REFRESH -> {
        uiDao
          .deleteAll()
          .subscribeOn(Schedulers.io())
          .flatMap {
            dao.get(0, state.config.pageSize)
              .map { it.map { it.toWordUI() } }
              .flatMap { uiDao.add(it) }
          }
          .map { MediatorResult.Success(false) as MediatorResult }
          .onErrorReturn {
            Logger.e(it)
            MediatorResult.Error(it)
          }
      }
      LoadType.PREPEND -> Single.just(MediatorResult.Success(true) as MediatorResult)
      LoadType.APPEND -> {
        val offset = state.pages.lastOrNull { it.data.isNotEmpty() }?.nextKey ?: 0
        dao
          .get(offset, state.config.pageSize)
          .subscribeOn(Schedulers.io())
          .map { it.map { it.toWordUI() } }
          .flatMap { uiDao.add(it) }
          .map { it.size < state.config.pageSize }
          .map { MediatorResult.Success(it) as MediatorResult }
          .onErrorReturn {
            Logger.e(it)
            MediatorResult.Error(it)
          }
      }
    }
  }

}