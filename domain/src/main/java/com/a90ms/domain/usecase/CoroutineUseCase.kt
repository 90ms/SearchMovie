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

package com.a90ms.domain.usecase

import com.a90ms.common.dto.CommonDto
import com.a90ms.common.dto.isSuccess
import com.a90ms.domain.base.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class CommonDtoUseCase<in P, R : Any>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(parameters: P): Result<R?> {
        return try {
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    if (it.code == "0" && it.data != null) {
                        Result.Success(it.data)
                    } else {
                        Result.Error(200, it.code, it.errorMessage)
                    }
                }
            }
        } catch (e: HttpException) {
            Result.Error(e.code(), "", e.message())
        } catch (e: Exception) {
            Result.Exception(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): CommonDto<R>
}

abstract class NoParamCommonDtoUseCase<R : Any>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                execute().let {
                    if (it.isSuccess()) {
                        Result.Success(it.data!!)
                    } else {
                        Result.Error(200, it.code, it.errorMessage)
                    }
                }
            }
        } catch (e: Exception) {
            Result.Exception(e)
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): CommonDto<R>
}
