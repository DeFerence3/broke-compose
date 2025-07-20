package com.diffy.broke.data.repository

import com.diffy.broke.data.dao.AccountGroupDao
import com.diffy.broke.data.entity.AccountGroup
import com.diffy.broke.domain.repository.AccountGroupRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountGroupRepoImpl @Inject constructor(
    private val accountGroupDao: AccountGroupDao
) : AccountGroupRepo {
    override suspend fun upsertAccountGroup(accountGroup: AccountGroup): Int {
        accountGroupDao.upsert(entity = accountGroup)
        return 7
    }

    override suspend fun searchAccountGroup(name: String): Flow<List<AccountGroup>> {
        return accountGroupDao.searchAccountGroupByName(name = name)
    }

    override suspend fun findAccountGroupById(id: Int): AccountGroup? {
        return accountGroupDao.findById(id)
    }
}