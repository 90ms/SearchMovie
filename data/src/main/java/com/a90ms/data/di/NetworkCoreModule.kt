package com.a90ms.data.di

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.a90ms.common.ext.quit
import com.a90ms.data.BuildConfig
import com.a90ms.data.R
import com.a90ms.data.api.DEFAULT_TIME_OUT
import com.a90ms.data.api.HOST_URL
import com.a90ms.data.api.NetworkConnectionInterceptor
import com.a90ms.data.api.ResponseCode.ERROR_429
import com.a90ms.data.api.ResponseCode.ERROR_SE01
import com.a90ms.data.api.ResponseCode.ERROR_SE02
import com.a90ms.data.api.ResponseCode.ERROR_SE03
import com.a90ms.data.api.ResponseCode.ERROR_SE04
import com.a90ms.data.api.ResponseCode.ERROR_SE05
import com.a90ms.data.api.ResponseCode.ERROR_SE06
import com.a90ms.data.api.ResponseCode.ERROR_SE99
import com.a90ms.data.api.ServerInspectionResponse
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkCoreModule {

    const val HOST_NAVER = "naverHost"

    @Provides
    @Named(HOST_NAVER)
    fun provideHostNaver() = HOST_URL

    @Provides
    @Singleton
    fun provideObjMatter(): ObjectMapper = ObjectMapper().apply {
        registerKotlinModule()
        registerModule(SimpleModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Provides
    @Singleton
    fun provideConverterFactory(objectMapper: ObjectMapper): JacksonConverterFactory =
        JacksonConverterFactory.create(objectMapper)

    private fun checkResponseAndReturn(response: Response, context: Context): Response {
        if (!response.isSuccessful) {
            handleServiceError(response, context)
        }

        return response
    }

//    private fun handleError(response: Response, context: Context) {
//        handleServiceError(re)
//        when (response.code) {
//            ERROR_400 -> {
//                // 400
//            }
//            ERROR_500 -> {
//                Handler(Looper.getMainLooper()).post {
//                    Toast.makeText(context, "인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
//                }
//            }
//            else -> {
//                response.body
//                showNetworkErrorToast(response, context)
//            }
//        }
//    }

    private var lastHandleServiceErrorAt = 0L

    const val SYNC_INTERVAL = 1000 * 5L

    @Synchronized
    private fun handleServiceError(response: Response, context: Context) {
        fun showNetworkErrorToast() = showNetworkErrorToast(response, context)

        val current = System.currentTimeMillis()
        if (current - lastHandleServiceErrorAt < SYNC_INTERVAL) return

        lastHandleServiceErrorAt = System.currentTimeMillis()
        try {
            val inspectionResponse = Gson().fromJson(
                response.body?.source()?.readUtf8(),
                ServerInspectionResponse::class.java
            )

            when (inspectionResponse.errorCode) {
                ERROR_SE01, ERROR_SE02, ERROR_SE03, ERROR_SE04, ERROR_SE05, ERROR_SE06 -> {
                    Handler(Looper.getMainLooper()).post {
                        val message = inspectionResponse.errorMessage
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
                ERROR_SE99 -> {
                    Handler(Looper.getMainLooper()).post {
                        val message = inspectionResponse.errorMessage
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                    context.quit()
                }
                ERROR_429 -> {
                    // TODO 속도제한 관련 이슈로 굳이 보여주지 않아도 됨
                }
                else -> {
                    showNetworkErrorToast()
                }
            }
        } catch (e: Exception) {
            showNetworkErrorToast()
        }
    }

    private fun showNetworkErrorToast(response: Response, context: Context) {
        Handler(Looper.getMainLooper()).post {
            val message = response.message.ifBlank { "일시적 오류가 발생하였습니다." }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Singleton
    @Provides
    fun provideNetworkConnectivityInterceptor(
        @ApplicationContext context: Context,
    ): NetworkConnectionInterceptor =
        NetworkConnectionInterceptor(
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )

    @Provides
    @Singleton
    @Named(HOST_NAVER)
    fun provideSimpleClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkConnectivityInterceptor: NetworkConnectionInterceptor,
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                Interceptor { chain ->
                    val requestBuilder =
                        chain
                            .request()
                            .newBuilder()
                            .addHeader(
                                "X-Naver-Client-Id",
                                context.getString(R.string.naver_client_id)
                            )
                            .addHeader(
                                "X-Naver-Client-Secret",
                                context.getString(R.string.naver_client_secret)
                            )

                    checkResponseAndReturn(
                        chain.proceed(requestBuilder.build()),
                        context
                    )
                }
            )
            .addInterceptor(networkConnectivityInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        return if (BuildConfig.DEBUG) {
            okHttpClient.build()
            okHttpClient.addNetworkInterceptor(StethoInterceptor()).build()
        } else {
            okHttpClient.build()
        }
    }

    @Provides
    @Singleton
    @Named(HOST_NAVER)
    fun provideRefreshRetrofitBuilder(
        jacksonConverterFactory: JacksonConverterFactory,
        @Named(HOST_NAVER) okHttpClient: OkHttpClient,
    ): Retrofit.Builder = Retrofit
        .Builder()
        .baseUrl(HOST_URL)
        .client(okHttpClient)
        .addConverterFactory(jacksonConverterFactory)
}
