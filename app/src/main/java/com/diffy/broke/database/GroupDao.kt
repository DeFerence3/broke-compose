package com.diffy.broke.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Upsert
    suspend fun upsertPack(group: Group)

    @Upsert
    suspend fun upsertTransaction(transactions: Transactions)

    @Delete
    suspend fun deletePack(group: Group) : Void

    @Delete
    suspend fun deleteTransaction(transactions: Transactions) : Void

    @Query("SELECT * FROM `group` ORDER BY totalExp ASC")
    fun getGroupsOrderByMost(): Flow<List<Group>>

    @Query("SELECT * FROM `group` ORDER BY totalExp DESC")
    fun getGroupsOrderByLeast(): Flow<List<Group>>

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

/*    @Upsert
    suspend fun upsertExpense(expense: Expense)



    @Transaction
    @Query("SELECT * FROM `group` WHERE id = :groupId ORDER BY totalExp ASC")
    fun getExpensesOrderByMost(groupId: Int): Flow<List<GroupWithExpense>>

    @Transaction
    @Query("SELECT * FROM `group` WHERE id = :groupId ORDER BY totalExp DESC")
    fun getExpensesOrderByLeast(groupId: Int): Flow<List<GroupWithExpense>>*/
}