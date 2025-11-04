@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diffy.broke.data.entity.Transaction
import com.diffy.broke.data.relations.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Dao
interface TransactionDao: GenericDao<Transaction> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertTransaction(transaction: Transaction): Long

    @androidx.room.Transaction
    @Query("SELECT * FROM `transaction` WHERE date BETWEEN :start AND :end")
    fun getAllTransactionsOnDateRange(start: Instant, end: Instant): Flow<List<TransactionWithCategory>>

    @androidx.room.Transaction
    @Query("SELECT * FROM `transaction`")
    fun getAllTransactions(): Flow<List<TransactionWithCategory>>
}