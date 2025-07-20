package com.diffy.broke.domain.repository

import com.diffy.broke.data.entity.AccountGroup
import kotlinx.coroutines.flow.Flow

interface AccountGroupRepo {
    suspend fun upsertAccountGroup(accountGroup: AccountGroup): Int
    suspend fun searchAccountGroup(name: String): Flow<List<AccountGroup>>
    suspend fun findAccountGroupById(id: Int): AccountGroup?
}