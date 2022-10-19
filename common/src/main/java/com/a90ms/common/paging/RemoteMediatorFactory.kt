package com.a90ms.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.a90ms.common.dto.CommonDto
import com.a90ms.common.dto.PageDto

object RemoteMediatorFactory {
    private const val DEFAULT_PAGE_SIZE = 10

    @OptIn(ExperimentalPagingApi::class)
    fun <Dto : Any, Entity : RemoteMediatorEntity> getPagingFlow(
        initialPage: Int = 1,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        loadData: suspend (Int) -> CommonDto<out PageDto<Dto>>,
        updateDB: suspend (refresh: Boolean, PageDto<Dto>, Int?, Int?) -> Unit,
        getKey: suspend (Entity) -> Entity?,
        pagingSource: () -> PagingSource<Int, Entity>,
        hasNextPage: ((PageDto<Dto>) -> Boolean)? = null,
    ) = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        remoteMediator = BaseRemoteMediator(
            initialPage = initialPage,
            loadData = loadData,
            updateDB = updateDB,
            getKey = getKey,
            hasNextPage = hasNextPage,
        ),
        pagingSourceFactory = pagingSource
    ).flow
}
