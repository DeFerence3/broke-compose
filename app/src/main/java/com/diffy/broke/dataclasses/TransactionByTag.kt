package com.diffy.broke.dataclasses

data class TransactionByTag(
    val tag: String = "",
    val totalAmount: Float = 0F
)