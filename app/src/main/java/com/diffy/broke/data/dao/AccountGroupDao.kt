package com.diffy.broke.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.diffy.broke.data.entity.AccountGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountGroupDao: GenericDao<AccountGroup> {

    @Query("SELECT * FROM account_group where name like :name || '%' ")
    fun searchAccountGroupByName(name: String): Flow<List<AccountGroup>>

    @Query("SELECT * FROM account_group WHERE id = :id")
    suspend fun findById(id: Int): AccountGroup?
}