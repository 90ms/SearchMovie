package com.a90ms.domain.repository

import androidx.paging.PagingSource
import com.a90ms.common.dto.CommonDto
import com.a90ms.common.dto.PageDto
import com.a90ms.domain.db.LocalMovieEntity
import com.a90ms.domain.dto.search.ItemsDto
import com.a90ms.domain.dto.search.ItemsPageDto
import kotlinx.coroutines.flow.Flow

interface NaverRepository {

    /**
     * 검색어로 영화 검색
     * */
    suspend fun getSearchList(
        query: String,
        display: Int,
        start: Int
    ): CommonDto<ItemsPageDto>

    /**
     * 즐겨찾기 목록만 검색
     * */
    fun getFavoriteList(): Flow<List<ItemsDto>>

    /**
     * DB에 remote response 밀어넣기
     * */
    suspend fun updateAllItems(
        refresh: Boolean,
        dto: PageDto<ItemsDto>,
        prevKey: Int?,
        nextKey: Int?
    )

    /**
     * 테이블 초기화
     * */
    suspend fun clearDatabase(): CommonDto<Unit>

    /**
     * 테이블 카운트
     * */
    fun countFromDB(): Flow<Int>

    /**
     * row 존재 여부
     * */
    fun existItem(): Flow<Boolean>

    /**
     * 단일 아이템 조회(페이징 키)
     * */
    suspend fun getItem(id: String): LocalMovieEntity?

    /**
     * 데이터 조회(페이징)
     * */
    fun getPagingSource(): PagingSource<Int, LocalMovieEntity>

    /**
     * 즐겨찾기 유무 업데이트
     * */
    suspend fun updateFavorite(id: String, value: Boolean): CommonDto<Unit>
}
