package com.a90ms.common.dto

abstract class PageDto<Value> {
    abstract val errorCode: String?
    abstract val errorMessage: String?
    abstract val content: List<Value>
    open val total: Int
        get() = 0
}
