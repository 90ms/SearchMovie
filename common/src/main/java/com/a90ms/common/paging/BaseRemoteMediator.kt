package com.a90ms.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.a90ms.common.dto.CommonDto
import com.a90ms.common.dto.PageDto

@OptIn(ExperimentalPagingApi::class)
class BaseRemoteMediator<Dto : Any, Entity : RemoteMediatorEntity>(
    private val initialPage: Int,
    private val loadData: suspend (Int) -> CommonDto<out PageDto<Dto>>,
    private val updateDB: suspend (refresh: Boolean, PageDto<Dto>, Int?, Int?) -> Unit,
    private val getKey: suspend (Entity) -> Entity?,
    private val hasNextPage: ((PageDto<Dto>) -> Boolean)? = null,
) : RemoteMediator<Int, Entity>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Entity>): MediatorResult {
        try {
            val page = getPage(loadType, state)
                ?: return MediatorResult.Success(false)

            val dto = loadData(page)
            dto.data?.let { data ->
                if (data.errorMessage.isNullOrBlank()) {
                    val hasNextPage: Boolean = hasNextPage?.let { it(data) }
                        ?: data.content.isNotEmpty()
                    val prevKey = if (page == 0) null else page - 1
                    val nextKey = if (!hasNextPage) null else page + 1

                    updateDB(loadType == LoadType.REFRESH, data, prevKey, nextKey)
                    return MediatorResult.Success(endOfPaginationReached = !hasNextPage)
                } else {
                    throw Exception(data.errorMessage)
                }
            } ?: run {
                throw Exception(dto.errorMessage)
            }
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getPage(
        loadType: LoadType,
        state: PagingState<Int, Entity>
    ) = when (loadType) {
        LoadType.REFRESH -> {
            getRemoteKeyClosestToCurrentPosition(state)?.nextKey?.minus(1) ?: initialPage
        }
        LoadType.PREPEND -> {
            getRemoteKeyForFirstItem(state)?.prevKey
        }
        LoadType.APPEND -> {
            getRemoteKeyForLastItem(state)?.nextKey
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Entity>
    ): Entity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let {
                getKey(it)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Entity>
    ): Entity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                getKey(it)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Entity>
    ): Entity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                getKey(it)
            }
    }
}
