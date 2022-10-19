package com.a90ms.data.di

import com.a90ms.data.api.NaverService
import com.a90ms.data.db.AppDatabase
import com.a90ms.data.db.search.MovieDao
import com.a90ms.data.repository.NaverRepositoryImpl
import com.a90ms.domain.repository.NaverRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideNaverRepository(
        naverService: NaverService,
        database: AppDatabase,
        movieDao: MovieDao
    ): NaverRepository = NaverRepositoryImpl(naverService, database, movieDao)
}
