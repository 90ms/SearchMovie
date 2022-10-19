package com.a90ms.data.api

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class NetworkConnectionInterceptor(
    private val connectivityManager: ConnectivityManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (isNetworkConnected()) {
            chain.proceed(chain.request())
        } else {
            Response.Builder()
                .protocol(Protocol.HTTP_1_1)
                .request(chain.request())
                .message("Network is not connected.")
                .body("".toResponseBody())
                .build()
        }
    }

    private fun isNetworkConnected(): Boolean =
        isNetworkConnectedFromM(connectivityManager)

    private fun isNetworkConnectedFromM(connectivityManager: ConnectivityManager): Boolean =
        with(connectivityManager) {
            val capabilities = getNetworkCapabilities(activeNetwork)

            capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                )
        }
}
