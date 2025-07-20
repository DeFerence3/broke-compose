package com.diffy.broke.domain.model

data class TransactionByTag(
    val tag: String = "",
    val totalAmount: Float = 0F
)