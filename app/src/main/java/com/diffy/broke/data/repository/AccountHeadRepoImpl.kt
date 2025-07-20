package com.diffy.broke.data.repository

import com.diffy.broke.data.dao.AccountHeadDao
import com.diffy.broke.data.entity.AccountHead
import com.diffy.broke.domain.repository.AccountHeadRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountHeadRepoImpl @Inject constructor(
    private val accountHeadDao: AccountHeadDao
) : AccountHeadRepo {
    override suspend fun upsertAccountHead(accountHead: AccountHead): Int {
        accountHeadDao.upsertAccountHead(accountHead = accountHead)
        return 7
    }

    override suspend fun searchAccountHead(name: String): Flow<List<AccountHead>> {
        return accountHeadDao.searchAccountHeadByName(name = name)
    }
}