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
import com.diffy.broke.dataclasses.TransactionByTag
import com.diffy.broke.dataclasses.MinMaxDates
import com.diffy.broke.dataclasses.SummaryData
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

    @Query("select tg.tag,SUM(tr.transAmnt) totalAmount from Tags tg\n" +
            "inner join TransactionWithTagsCrossRef ttr on ttr.tagid = tg.tagid\n" +
            "inner join Transactions tr on tr.id = ttr.id\n" +
            "where tr.isExp = 1 and tr.day between :start and :end\n" +
            "group by tg.tag\n" +
            "order by SUM(tr.transAmnt) desc")
    fun getExpenseByTag(start: Long, end: Long): Flow<List<TransactionByTag>>
    @Query("select tg.tag,SUM(tr.transAmnt) totalAmount from Tags tg\n" +
            "inner join TransactionWithTagsCrossRef ttr on ttr.tagid = tg.tagid\n" +
            "inner join Transactions tr on tr.id = ttr.id\n" +
            "where tr.isExp = 0 and tr.day between :start and :end\n" +
            "group by tg.tag\n" +
            "order by SUM(tr.transAmnt) desc")
    fun getIncomeByTag(start: Long, end: Long): Flow<List<TransactionByTag>>

    @Query("SELECT \n" +
            "    (SELECT SUM(CASE WHEN isExp = 0 THEN transAmnt ELSE 0 END) - \n" +
            "            SUM(CASE WHEN isExp = 1 THEN transAmnt ELSE 0 END) \n" +
            "     FROM Transactions where day between :start and :end ) as savings,\n" +
            "    SUM(CASE WHEN isExp = 1 THEN transAmnt ELSE 0 END) as totalSpend,\n" +
            "    SUM(CASE WHEN isExp = 0 THEN transAmnt ELSE 0 END) as totalEarn FROM Transactions where day between :start and :end")
    fun getOverView(start: Long, end: Long): Flow<SummaryData>
    @Query("SELECT * FROM TAGS where tag like :tag ")
    fun getTagsByTags(tag: String): Flow<List<Tags>>
}