package com.a90ms.common.dto

data class CommonDto<T : Any>(
    val code: String,
    val errorMessage: String,
    val data: T?
)

fun <T : Any> CommonDto<T>.isSuccess() = code == "200" && data != null
