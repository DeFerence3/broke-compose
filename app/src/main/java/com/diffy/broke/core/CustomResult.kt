package com.diffy.broke.core

sealed class Result<out T> {
    data class success<out T>(val data: T) : Result<T>()
    data class failure(val e: Throwable) : Result<Nothing>()
}