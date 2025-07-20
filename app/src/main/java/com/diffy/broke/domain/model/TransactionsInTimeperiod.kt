@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class TransactionsInTimeperiod(
    val day: Instant,
    val rangedTransactions: List<Transaction>
)