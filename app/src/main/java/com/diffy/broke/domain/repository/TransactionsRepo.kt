package com.diffy.broke.domain.repository

import com.diffy.broke.data.entity.Transaction
import com.diffy.broke.data.relations.TransactionWithAccountHeads
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface TransactionsRepo {

    fun getTransactionInDateRange(startTime: Instant, endTime: Instant): Flow<List<TransactionWithAccountHeads>>

    suspend fun getExpenseOrIncomeOnDateRange(
        startTimeMillis: Instant,
        endTimeMillis: Instant,
        isExp: Int
    ): Flow<List<TransactionWithAccountHeads>>

    suspend fun createTransaction(transaction: com.diffy.broke.data.entity.Transaction): Boolean

    fun getAllTransaction(): Flow<List<TransactionWithAccountHeads>>
    suspend fun deleteTransaction(transaction: Transaction)

}