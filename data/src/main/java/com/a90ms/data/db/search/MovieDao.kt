package com.a90ms.data.db.search

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.a90ms.data.db.BaseDao
import com.a90ms.domain.db.LocalMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao : BaseDao<LocalMovieEntity> {

    @Query("SELECT * FROM movie_table")
    fun pagingSource(): PagingSource<Int, LocalMovieEntity>

    @Query("SELECT * FROM movie_table WHERE favorite = :favorite")
    fun getFavoriteList(favorite: Boolean = true): Flow<List<LocalMovieEntity>>

    @Query("SELECT count(*) FROM movie_table")
    fun count(): Flow<Int>

    @Query("SELECT * FROM movie_table WHERE id = :id")
    suspend fun getItem(id: String): LocalMovieEntity?

    @Query("SELECT EXISTS(SELECT * FROM movie_table)")
    fun itemExist(): Flow<Boolean>

    @Query("DELETE FROM movie_table")
    suspend fun clear()

    @Query("UPDATE movie_table SET favorite = :favorite WHERE id = :id")
    suspend fun updateFavorite(id: String, favorite: Boolean)
}
