package com.diffy.broke.core

sealed class BrokeResponse<out T> {
    data class Loading(val message: String) : BrokeResponse<Nothing>()
    data class Error(val message: String) : BrokeResponse<Nothing>()
    data class Success<out T>(val data: T) : BrokeResponse<T>()
}