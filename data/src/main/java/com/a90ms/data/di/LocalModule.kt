package com.a90ms.data.di

import android.content.Context
import com.a90ms.data.db.AppDatabase
import com.a90ms.data.db.search.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideCoreDataBase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.buildDatabase(context)

    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()
}
