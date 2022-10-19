package com.a90ms.data.repository

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.a90ms.common.dto.CommonDto
import com.a90ms.common.dto.PageDto
import com.a90ms.data.api.NaverService
import com.a90ms.data.api.SUCCESS_CODE
import com.a90ms.data.db.AppDatabase
import com.a90ms.data.db.search.MovieDao
import com.a90ms.domain.db.LocalMovieEntity
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.domain.dto.search.ItemsPageDto
import com.a90ms.domain.repository.NaverRepository
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NaverRepositoryImpl(
    private val service: NaverService,
    private val database: AppDatabase,
    private val movieDao: MovieDao
) : NaverRepository {

    override suspend fun getSearchList(
        query: String,
        display: Int,
        start: Int
    ): CommonDto<ItemsPageDto> = if (start > 0) {
        service.getSearchList(query, display, start).run {
            CommonDto(errorCode ?: SUCCESS_CODE, errorMessage ?: "", this.toDto())
        }
    } else {
        CommonDto("", "", null)
    }

    override fun getFavoriteList(): Flow<List<ItemsDto>> =
        movieDao.getFavoriteList().map {
            it.map(LocalMovieEntity::toDto)
        }

    override suspend fun updateAllItems(
        refresh: Boolean,
        dto: PageDto<ItemsDto>,
        prevKey: Int?,
        nextKey: Int?
    ) {
        database.withTransaction {
            if (refresh) {
                movieDao.clear()
            }
            movieDao.insertAll(
                dto.content.map {
                    val randomId = UUID.randomUUID().toString()
                    val entity = LocalMovieEntity.fromDto(
                        it.copy(id = randomId, favorite = false),
                        prevKey,
                        nextKey
                    )
                    entity
                }
            )
        }
    }

    override suspend fun clearDatabase(): CommonDto<Unit> {
        return database.clearDB().run {
            CommonDto(SUCCESS_CODE, "", Unit)
        }
    }

    override fun countFromDB(): Flow<Int> = movieDao.count()
    override fun existItem(): Flow<Boolean> = movieDao.itemExist()
    override suspend fun getItem(id: String) = movieDao.getItem(id)
    override fun getPagingSource(): PagingSource<Int, LocalMovieEntity> =
        movieDao.pagingSource()

    override suspend fun updateFavorite(id: String, value: Boolean): CommonDto<Unit> {
        return movieDao.updateFavorite(id, !value).run {
            CommonDto(SUCCESS_CODE, "", Unit)
        }
    }
}
