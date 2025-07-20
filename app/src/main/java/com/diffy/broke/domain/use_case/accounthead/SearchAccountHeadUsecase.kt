package com.diffy.broke.domain.use_case.accounthead


import com.diffy.broke.domain.model.AccountGroup
import com.diffy.broke.domain.model.AccountHead
import com.diffy.broke.domain.repository.AccountGroupRepo
import com.diffy.broke.domain.repository.AccountHeadRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchAccountHeadUsecase @Inject constructor(
    private val accountHeadRepo: AccountHeadRepo,
    private val accountGroupRepo: AccountGroupRepo
) {
    operator fun invoke(accountHeadName: String): Flow<List<AccountHead>> =
        flow {
            val heads: Flow<List<com.diffy.broke.data.entity.AccountHead>> =  accountHeadRepo.searchAccountHead(name = accountHeadName)
            heads.collect { list ->
                emit( list.map {
                    val accountGroup = accountGroupRepo.findAccountGroupById(it.accountGroupId)!!
                    AccountHead(
                        id = it.id,
                        accountHeadName = it.accountHeadName,
                        accountGroup = AccountGroup(
                            id = accountGroup.id,
                            accountGroupName = accountGroup.name,
                            accountHeads = emptyList(),
                            classification = accountGroup.classification,
                            description = accountGroup.description
                        )
                    )
                } )
            }
        }
}