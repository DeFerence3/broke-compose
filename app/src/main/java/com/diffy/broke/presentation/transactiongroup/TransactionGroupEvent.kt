package com.diffy.broke.presentation.transactiongroup

sealed interface TransactionGroupEvent {
    data class Success(val message: String) : TransactionGroupEvent
    data class Error(val message: String) : TransactionGroupEvent
}