package com.a90ms.data.di

import com.a90ms.data.api.NaverService
import com.a90ms.data.di.NetworkCoreModule.HOST_NAVER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkServiceModule {

    @Singleton
    @Provides
    fun provideNaverService(
        @Named(HOST_NAVER)
        retrofitBuilder: Retrofit.Builder
    ): NaverService = retrofitBuilder.build().create(NaverService::class.java)
}
