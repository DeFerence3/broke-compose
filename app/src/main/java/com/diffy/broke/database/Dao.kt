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
    fun getTransactionsOrderByMost(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` ORDER BY transAmnt DESC")
    fun getTransactionsOrderByLeast(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' ORDER BY transAmnt DESC")
    fun getIncomeOrderByLeast(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' ORDER BY transAmnt DESC")
    fun getExpenseOrderByLeast(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '0' ORDER BY transAmnt ASC")
    fun getIncomeOrderByMost(): Flow<List<Transactions>>

    @Query("SELECT * FROM `transactions` WHERE isExp = '1' ORDER BY transAmnt ASC")
    fun getExpenseOrderByMost(): Flow<List<Transactions>>
}