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

    @Query("SELECT * FROM `transactions` WHERE isExp = :isExp and day BETWEEN :start AND :end")
    fun getExpenseOrIncomeOnDateRange(start: Long, end: Long, isExp: Int): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end")
    fun getAllTransactionsOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT MIN(day) minValue,MAX(day) maxValue FROM `transactions`")
    fun getMinMaxDate(): Flow<MinMaxDates>
}