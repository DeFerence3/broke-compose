@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diffy.broke.data.entity.Transaction
import com.diffy.broke.data.relations.TransactionWithCategory
import com.diffy.broke.domain.model.OverView
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

    @Query("""
    select SUM(t.amount) totalSpend,IFNULL((select mb.budget from monthly_budget mb where mb.year = :year and mb.month = :month),0) budget  from `transaction` t
    where CAST(strftime('%m', t.date) AS INTEGER) = :month AND CAST(strftime('%Y', t.date) AS INTEGER) = :year
    """)
    fun getOverView(month: Int, year: Int): Flow<OverView>

    @Query("SELECT SUM(t.amount) FROM `transaction` t where date(t.date) = date(:date)")
    fun getSpendByDate(date: Instant): Flow<Double>
}