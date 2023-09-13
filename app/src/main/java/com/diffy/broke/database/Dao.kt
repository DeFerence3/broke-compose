package com.diffy.broke.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Upsert
    suspend fun upsertTransaction(transactions: Transactions)

    @Delete
    suspend fun deleteTransaction(transactions: Transactions) : Void

    @Query("SELECT * FROM `transactions` ORDER BY transAmnt ASC ")
    fun getTransactionsOrderByAscend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` ORDER BY transAmnt DESC")
    fun getTransactionsOrderByDescend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' ORDER BY transAmnt DESC")
    fun getIncomeOrderByDescend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' and day BETWEEN :start AND :end ORDER BY transAmnt DESC")
    fun getIncomeOrderByDescendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' ORDER BY transAmnt DESC")
    fun getExpenseOrderByDescend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' and day BETWEEN :start AND :end ORDER BY transAmnt DESC")
    fun getExpenseOrderByDescendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' ORDER BY transAmnt ASC")
    fun getIncomeOrderByAscend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' and day BETWEEN :start AND :end ORDER BY transAmnt ASC")
    fun getIncomeOrderByAscendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' ORDER BY transAmnt ASC")
    fun getExpenseOrderByAscend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' and day BETWEEN :start AND :end ORDER BY transAmnt ASC")
    fun getExpenseOrderByAscendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end ORDER BY transAmnt DESC")
    fun getTransactionsOrderByDescendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end ORDER BY transAmnt ASC")
    fun getTransactionsOrderByAscendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>
}