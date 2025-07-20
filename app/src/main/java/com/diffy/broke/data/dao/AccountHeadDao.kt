package com.diffy.broke.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.diffy.broke.data.entity.AccountHead
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountHeadDao: GenericDao<AccountHead> {

    @Upsert()
    fun upsertAccountHead(accountHead: AccountHead)

    @Query("SELECT * FROM account_head where name like :name || '%' ")
    fun searchAccountHeadByName(name: String): Flow<List<AccountHead>>

}