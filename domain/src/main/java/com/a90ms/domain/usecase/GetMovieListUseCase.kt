package com.a90ms.domain.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import com.a90ms.common.base.CALL_PAGE_COUNT
import com.a90ms.common.base.PAGE_SIZE
import com.a90ms.common.paging.RemoteMediatorFactory
import com.a90ms.domain.db.LocalMovieEntity
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.domain.dto.search.ItemsPageDto
import com.a90ms.domain.repository.NaverRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMovieListUseCase @Inject constructor(
    private val repository: NaverRepository
) {
    @ExperimentalPagingApi
    operator fun invoke(query: String): Flow<PagingData<ItemsDto>> =
        RemoteMediatorFactory.getPagingFlow(
            pageSize = PAGE_SIZE,
            loadData = { start ->
                repository.getSearchList(
                    query = query,
                    display = CALL_PAGE_COUNT,
                    start = start
                )
            },
            pagingSource = { repository.getPagingSource() },
            updateDB = { refresh, pageDto, prev, next ->
                repository.updateAllItems(
                    refresh,
                    pageDto,
                    prev,
                    next
                )
            },
            getKey = { repository.getItem(it.id) },
            hasNextPage = {
                val pageDto = it as ItemsPageDto
                pageDto.currentPage < pageDto.maxPage
            }
        ).map {
            it.map(LocalMovieEntity::toDto)
        }

    fun count() = repository.countFromDB()

    fun exist() = repository.existItem()
}
