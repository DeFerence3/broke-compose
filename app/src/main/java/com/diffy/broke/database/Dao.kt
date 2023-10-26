package com.diffy.broke.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.diffy.broke.database.relations.TransactionWithTags
import com.diffy.broke.database.relations.TransactionWithTagsCrossRef
import com.diffy.broke.dataclasses.MinMaxDates
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertTransaction(transactions: Transactions): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertTags(tag: List<Tags>): List<Long>

    @Upsert
    fun upsertTransactionsWithTags(transTagCrossRef: List<TransactionWithTagsCrossRef>)

    @Delete
    suspend fun deleteTransaction(transactions: Transactions) : Void

    @Query("SELECT * FROM `transactions` WHERE isExp = :isExp and day BETWEEN :start AND :end")
    fun getExpenseOrIncomeOnDateRange(start: Long, end: Long, isExp: Int): Flow<List<TransactionWithTags>>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end")
    fun getAllTransactionsOnDateRange(start: Long, end: Long): Flow<List<Transactions>>

    @Query("SELECT MIN(day) minValue,MAX(day) maxValue FROM `transactions`")
    fun getMinMaxDate(): Flow<MinMaxDates>

    @Transaction
    @Query("SELECT * FROM Transactions")
    fun getTransactionWithTags(): List<TransactionWithTags>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end")
    fun getTransactionsWithTagsOnDateRange(start: Long, end: Long): Flow<List<TransactionWithTags>>

    @Query("SELECT * FROM `transactions` WHERE day BETWEEN :start AND :end")
    fun getOneTransactionWithTags(start: Long, end: Long): Flow<List<TransactionWithTags>>
}