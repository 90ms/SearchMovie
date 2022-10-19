package com.a90ms.data.api

import com.a90ms.data.entity.search.ItemsPageEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverService {

    @GET("v1/search/movie.json")
    suspend fun getSearchList(
        @Query(QUERY) query: String,
        @Query(QUERY_DISPLAY) display: Int,
        @Query(QUERY_START) start: Int,
    ): ItemsPageEntity

    companion object {
        /**
         * 검색어
         * */
        const val QUERY = "query"

        /**
         * size
         * */
        const val QUERY_DISPLAY = "display"

        /**
         * page
         * */
        const val QUERY_START = "start"
    }
}
