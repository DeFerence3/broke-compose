@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.data.repository

import androidx.room.Transaction
import com.diffy.broke.data.dao.TransactionDao
import com.diffy.broke.data.relations.TransactionWithCategory
import com.diffy.broke.domain.repository.TransactionsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class TransactionsRepoImpl @Inject constructor(
    private val transactionDao: TransactionDao
) :TransactionsRepo {
    override fun getTransactionInDateRange(
        startTime: Instant,
        endTime: Instant
    ): Flow<List<TransactionWithCategory>> {
        return transactionDao.getAllTransactionsOnDateRange(startTime,endTime)
    }

    override suspend fun getExpenseOrIncomeOnDateRange(
        startTimeMillis: Instant,
        endTimeMillis: Instant,
        isExp: Int
    ): Flow<List<TransactionWithCategory>> {
        return transactionDao.getAllTransactionsOnDateRange(startTimeMillis,endTimeMillis)
    }

    @Transaction
    override suspend fun createTransaction(transaction: com.diffy.broke.data.entity.Transaction): Boolean {
        val transid = transactionDao.upsertTransaction(transaction)
        return transid != 0L
    }

    override fun getAllTransaction(): Flow<List<TransactionWithCategory>> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun deleteTransaction(transaction: com.diffy.broke.data.entity.Transaction) {
        transactionDao.delete(transaction)
    }
}