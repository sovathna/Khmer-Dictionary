package io.github.sovathna.khmerdictionary.data.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.github.sovathna.khmerdictionary.domain.database.AppDatabase
import io.github.sovathna.khmerdictionary.model.HomeEntity
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class HomeMediator @Inject constructor(
    appDatabase: AppDatabase
) : RemoteMediator<Int, HomeEntity>() {

    private val word = appDatabase.wordDao()
    private val home = appDatabase.homeDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HomeEntity>
    ): MediatorResult {
        Timber.d("load: $loadType")

        return if (loadType == LoadType.APPEND) {
            val offset = home.count()
            val data = word.words(offset, state.config.pageSize).map { it.toHomeEntity() }
            home.addAll(data)
            MediatorResult.Success(endOfPaginationReached = data.size < state.config.pageSize)
        }  else MediatorResult.Success(true)

    }

    override suspend fun initialize(): InitializeAction = InitializeAction.SKIP_INITIAL_REFRESH
}