package com.diffy.broke.domain.repository

import com.diffy.broke.data.entity.AccountHead
import kotlinx.coroutines.flow.Flow

interface AccountHeadRepo {
    suspend fun upsertAccountHead(accountHead: AccountHead): Int
    suspend fun searchAccountHead(name: String): Flow<List<AccountHead>>
}