package com.diffy.broke.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.diffy.broke.dataclasses.MinMaxDates
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Upsert
    suspend fun upsertTransaction(transactions: Transactions)

    @Delete
    suspend fun deleteTransaction(transactions: Transactions) : Void

    @Query("SELECT * FROM `transactions` ORDER BY day ASC ")
    fun getTransactionsOrderByAscend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` ORDER BY day DESC")
    fun getTransactionsOrderByDescend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' ORDER BY day DESC")
    fun getIncomeOrderByDescend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' and day BETWEEN :start AND :end ORDER BY day DESC")
    fun getIncomeOrderByDescendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' ORDER BY day DESC")
    fun getExpenseOrderByDescend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' and day BETWEEN :start AND :end ORDER BY day DESC")
    fun getExpenseOrderByDescendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' ORDER BY day ASC")
    fun getIncomeOrderByAscend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' and day BETWEEN :start AND :end ORDER BY day ASC")
    fun getIncomeOrderByAscendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' ORDER BY day ASC")
    fun getExpenseOrderByAscend(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' and day BETWEEN :start AND :end ORDER BY day ASC")
    fun getExpenseOrderByAscendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end ORDER BY day DESC")
    fun getTransactionsOrderByDescendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end ORDER BY day ASC")
    fun getTransactionsOrderByAscendOnDateRange(start: Long, end: Long): Flow<List<Transactions>>
    @Query("SELECT MIN(day) AS minValue, MAX(day) AS maxValue FROM `transactions`")
    fun getMinAndMaxDatetime(): Flow<MinMaxDates>
}