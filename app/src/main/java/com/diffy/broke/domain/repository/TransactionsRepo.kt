package com.diffy.broke.domain.repository

import com.diffy.broke.data.entity.Transaction
import com.diffy.broke.data.relations.TransactionWithCategory
import com.diffy.broke.domain.model.OverView
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface TransactionsRepo {

    fun getTransactionInDateRange(startTime: Instant, endTime: Instant): Flow<List<TransactionWithCategory>>

    suspend fun getExpenseOrIncomeOnDateRange(
        startTimeMillis: Instant,
        endTimeMillis: Instant,
        isExp: Int
    ): Flow<List<TransactionWithCategory>>

    suspend fun createTransaction(transaction: com.diffy.broke.data.entity.Transaction): Boolean

    fun getAllTransaction(): Flow<List<TransactionWithCategory>>
    suspend fun deleteTransaction(transaction: Transaction)

    fun getOverView(month: Int,year: Int): Flow<OverView>

    fun getTodaySpend(): Flow<Double>

}