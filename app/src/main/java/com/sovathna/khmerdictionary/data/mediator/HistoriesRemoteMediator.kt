package com.sovathna.khmerdictionary.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.sovathna.androidmvi.Logger
import com.sovathna.khmerdictionary.data.local.db.LocalDatabase
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

@OptIn(ExperimentalPagingApi::class)
class HistoriesRemoteMediator(
  private val local: LocalDatabase
) : RxRemoteMediator<Int, HistoryUI>() {

  private val dao = local.historyDao()
  private val uiDao = local.historyUIDao()

  override fun loadSingle(
    loadType: LoadType,
    state: PagingState<Int, HistoryUI>
  ): Single<MediatorResult> {
    return when (loadType) {
      LoadType.REFRESH -> {
        uiDao
          .clear()
          .subscribeOn(Schedulers.io())
          .flatMap { dao.get(0, state.config.pageSize) }
          .map { it.map { it.toHistoryUI() } }
          .flatMap { uiDao.add(it) }
          .map { MediatorResult.Success(it.size < state.config.pageSize) }
          .cast(MediatorResult::class.java)
          .onErrorReturn { Logger.e(it); MediatorResult.Error(it) }
      }
      LoadType.PREPEND -> Single.just(MediatorResult.Success(true))
        .cast(MediatorResult::class.java)
      LoadType.APPEND -> {
        val offset = state.pages.lastOrNull { it.data.isNotEmpty() }?.nextKey ?: 0
        dao
          .get(offset, state.config.pageSize)
          .subscribeOn(Schedulers.io())
          .map { it.map { it.toHistoryUI() } }
          .flatMap { uiDao.add(it) }
          .map { MediatorResult.Success(it.size < state.config.pageSize) }
          .cast(MediatorResult::class.java)
          .onErrorReturn { Logger.e(it); MediatorResult.Error(it) }
      }
    }
  }

}