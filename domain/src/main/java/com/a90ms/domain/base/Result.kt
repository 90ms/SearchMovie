/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.a90ms.domain.base

import com.a90ms.domain.base.Result.Success

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val httpCode: Int,
        val errorCode: String,
        val message: String
    ) : Result<Nothing>()
    data class Exception(val exception: kotlin.Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Exception -> "Exception[exception=$exception]"
            is Error -> "Error[httpCode=$httpCode, errorCode=$errorCode, message=$message]"
        }
    }
}

inline fun <reified T> Result<T>.onSuccess(action: (data: T) -> Unit): Result<T> {
    if (this is Success) {
        action(data)
    }

    return this
}

inline fun <reified T> Result<T>.onError(
    action: (code: String, message: String) -> Unit
): Result<T> {
    if (this is Result.Error && httpCode in 200..500) {
        action(errorCode, message)
    }

    return this
}

inline fun <reified T> Result<T>.onException(
    action: (exception: Exception) -> Unit
): Result<T> {
    if (this is Result.Exception) {
        action(exception)
    }

    return this
}
